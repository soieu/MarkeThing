package com.example.demo.payment.service.impl;

import com.example.demo.exception.MarkethingException;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.marketpurchaserequest.repository.MarketPurchaseRequestRepository;
import com.example.demo.payment.dto.*;
import com.example.demo.payment.entity.Pay;
import com.example.demo.payment.repository.PaymentRepository;
import com.example.demo.payment.service.PaymentService;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static com.example.demo.exception.type.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final IamportClient iamportClient;
    private final MarketPurchaseRequestRepository marketPurchaseRequestRepository;
    private final SiteUserRepository siteUserRepository;

    @Override
    public RequestPayDto findRequestDto(Long orderUid) {

        MarketPurchaseRequest marketPurchaseRequest = marketPurchaseRequestRepository.findById(orderUid)
                .orElseThrow(() -> new MarkethingException(ORDER_NOT_EXIST));

        return RequestPayDto.builder()
                .buyerName(marketPurchaseRequest.getSiteUser().getName())
                .paymentPrice((long) marketPurchaseRequest.getPay().getAmount())
                .itemName(marketPurchaseRequest.getTitle())
                .orderUid(String.valueOf(marketPurchaseRequest.getId()))
                .build();
    }

    @Override
    public IamportResponse<Payment> paymentByCallback(PaymentCallbackRequestDto request) {
        try {
            // 결제 단건 조회(아임포트)
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(String.valueOf(request.getPaymentUid()));
            // 주문내역 조회
            MarketPurchaseRequest marketPurchaseRequest = marketPurchaseRequestRepository.findById(request.getOrderUid())
                    .orElseThrow(() -> new MarkethingException(ORDER_NOT_EXIST));

            // 결제 완료가 아니면
            if(!iamportResponse.getResponse().getStatus().equals("paid")) {
                // 주문, 결제 삭제
                paymentRepository.delete(marketPurchaseRequest.getPay());

                throw new MarkethingException(PAYMENT_INCOMPLETE);
            }

            int amount = marketPurchaseRequest.getPay().getAmount();
            int iamportPrice = iamportResponse.getResponse().getAmount().intValue();

            if(iamportPrice != amount) {
                paymentRepository.delete(marketPurchaseRequest.getPay());

                iamportClient.cancelPaymentByImpUid(new CancelData(iamportResponse.getResponse().getImpUid(), true, new BigDecimal(iamportPrice)));

                throw new MarkethingException(SUSPECT_PAYMENT_FORGERY);
            }

            // 결제 상태 변경
            marketPurchaseRequest.getPay().changePaymentBySuccess();

            return iamportResponse;

        } catch (IamportResponseException e) {
            throw new MarkethingException(IAMPORT_ERROR);
        } catch (IOException e) {
            throw new MarkethingException(ORDER_NOT_EXIST);
        }
    }

    @Override
    public IamportResponse<Payment> cancelPayment(Long paymentId, CancelPaymentRequestDto request) {
        try {
            // 결제 정보 조회
            Pay pay = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new MarkethingException(PAYMENT_NOT_FOUND));

            // 아임포트 결제 취소 요청
            CancelData cancelData = new CancelData(pay.getImpUid(), true, new BigDecimal(request.getAmount()));
            cancelData.setReason(request.getReason());
            IamportResponse<Payment> response = iamportClient.cancelPaymentByImpUid(cancelData);

            // 결제 취소 실패시 예외 발생
            if (!"cancelled".equals(response.getResponse().getStatus())) {
                throw new MarkethingException(PAYMENT_CANCEL_INCOMPLETE);
            }

            // 결제 상태 변경
            pay.changePaymentByCancel();
            paymentRepository.save(pay);

            return response;
        } catch (IamportResponseException | IOException e) {
            // 결제 취소 실패
            throw new MarkethingException(IAMPORT_ERROR);
        }
    }

    @Override
    public List<PayResponseDto> listPayment(PaymentListRequestDto paymentListRequestDto) {
        return paymentRepository.findPayResponseDtoById(paymentListRequestDto.getUserId());
    }
}