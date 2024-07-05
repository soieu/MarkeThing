package com.example.demo.payment.service;

import com.example.demo.entity.MarketPurchaseRequest;
import com.example.demo.exception.MarkethingException;
import com.example.demo.payment.dto.PaymentCallbackRequest;
import com.example.demo.payment.dto.RequestPayDto;
import com.example.demo.payment.repository.MarketPurchaseRequestRepository;
import com.example.demo.payment.repository.PaymentRepository;
import com.example.demo.type.PaymentStatus;
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

import static com.example.demo.exception.type.ErrorCode.IAMPORT_ERROR;
import static com.example.demo.exception.type.ErrorCode.SUSPECT_PAYMENT_FORGERY;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final IamportClient iamportClient;
    private final MarketPurchaseRequestRepository marketPurchaseRequestRepository;

    @Override
    public RequestPayDto findRequestDto(String orderUid) {

        MarketPurchaseRequest marketPurchaseRequest = marketPurchaseRequestRepository.findOrderAndPaymentAndMember(orderUid)
                .orElseThrow(() -> new IllegalArgumentException("주문이 없습니다."));

        return RequestPayDto.builder()
                .buyerName(marketPurchaseRequest.getSiteUser().getName())
                .paymentPrice(marketPurchaseRequest.getPayment().getAmount())
                .itemName(marketPurchaseRequest.getTitle())
                .orderUid(marketPurchaseRequest.getOrderUid())
                .build();
    }

    @Override
    public IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request) {
        try {
            // 결제 단건 조회(아임포트)
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(request.getPaymentUid());
            // 주문내역 조회
            MarketPurchaseRequest marketPurchaseRequest = marketPurchaseRequestRepository.findOrderAndPayment(request.getOrderUid())
                    .orElseThrow(() -> new IllegalArgumentException("주문 내역이 없습니다."));

            // 결제 완료가 아니면
            if(!iamportResponse.getResponse().getStatus().equals("paid")) {
                // 주문, 결제 삭제
                marketPurchaseRequestRepository.delete(marketPurchaseRequest);
                paymentRepository.delete(marketPurchaseRequest.getPayment());

                throw new RuntimeException("결제 미완료");
            }

            // DB에 저장된 결제 금액
            Long amount = marketPurchaseRequest.getPayment().getAmount();
            // 실 결제 금액
            int iamportPrice = iamportResponse.getResponse().getAmount().intValue();

            // 결제 금액 검증
            if(iamportPrice != amount) {
                // 주문, 결제 삭제
                marketPurchaseRequestRepository.delete(marketPurchaseRequest);
                paymentRepository.delete(marketPurchaseRequest.getPayment());

                // 결제금액 위변조로 의심되는 결제금액을 취소(아임포트)
                iamportClient.cancelPaymentByImpUid(new CancelData(iamportResponse.getResponse().getImpUid(), true, new BigDecimal(iamportPrice)));

                throw new MarkethingException(SUSPECT_PAYMENT_FORGERY);
            }

            // 결제 상태 변경
            marketPurchaseRequest.getPayment().changePaymentBySuccess();

            return iamportResponse;

        } catch (IamportResponseException e) {
            throw new MarkethingException(IAMPORT_ERROR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
