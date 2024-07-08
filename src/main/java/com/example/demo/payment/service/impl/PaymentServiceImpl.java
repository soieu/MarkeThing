package com.example.demo.payment.service.impl;

import com.example.demo.exception.MarkethingException;

import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.marketpurchaserequest.repository.MarketPurchaseRequestRepository;
import com.example.demo.payment.dto.PaymentCallbackRequest;
import com.example.demo.payment.dto.RequestPayDto;
import com.example.demo.payment.repository.PaymentRepository;
import com.example.demo.payment.service.PaymentService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

import static com.example.demo.exception.type.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final IamportClient iamportClient;
    private final MarketPurchaseRequestRepository marketPurchaseRequestRepository;

    @Override
    public RequestPayDto findRequestDto(String orderUid) {

        MarketPurchaseRequest marketPurchaseRequest = marketPurchaseRequestRepository.findRequestAndPaymentAndMember(orderUid)
                .orElseThrow(() -> new IllegalArgumentException("주문이 없습니다."));

        return RequestPayDto.builder()
                .buyerName(marketPurchaseRequest.getSiteUser().getName())
                .paymentPrice(marketPurchaseRequest.getPayment().getAmount())
                .itemName(marketPurchaseRequest.getTitle())
                .orderUid(String.valueOf(marketPurchaseRequest.getId()))
                .build();
    }

    @Override
    public IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request) {
        try {
            // 결제 단건 조회(아임포트)
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(request.getPaymentUid());
            // 주문내역 조회
            MarketPurchaseRequest marketPurchaseRequest = marketPurchaseRequestRepository.findRequestAndPayment(request.getOrderUid())
                    .orElseThrow(() -> new MarkethingException(ORDER_NOT_EXIST));

            // 결제 완료가 아니면
            if(!iamportResponse.getResponse().getStatus().equals("paid")) {
                // 주문, 결제 삭제
                marketPurchaseRequestRepository.delete(marketPurchaseRequest);
                paymentRepository.delete(marketPurchaseRequest.getPayment());

                throw new MarkethingException(PAYMENT_INCOMPLETE);
            }

            int amount = marketPurchaseRequest.getPayment().getAmount();
            int iamportPrice = iamportResponse.getResponse().getAmount().intValue();

            if(iamportPrice != amount) {
                marketPurchaseRequestRepository.delete(marketPurchaseRequest);
                paymentRepository.delete(marketPurchaseRequest.getPayment());

                iamportClient.cancelPaymentByImpUid(new CancelData(iamportResponse.getResponse().getImpUid(), true, new BigDecimal(iamportPrice)));

                throw new MarkethingException(SUSPECT_PAYMENT_FORGERY);
            }

            // 결제 상태 변경
            marketPurchaseRequest.getPayment().changePaymentBySuccess();

            return iamportResponse;

        } catch (IamportResponseException e) {
            throw new MarkethingException(IAMPORT_ERROR);
        } catch (IOException e) {
            throw new MarkethingException(ORDER_NOT_EXIST);
        }
    }



}