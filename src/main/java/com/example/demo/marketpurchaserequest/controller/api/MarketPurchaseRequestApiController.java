package com.example.demo.marketpurchaserequest.controller.api;

import com.example.demo.marketpurchaserequest.dto.MarketPurchaseRequestDto;
import com.example.demo.marketpurchaserequest.service.MarketPurchaseRequestService;
import com.example.demo.marketpurchaserequest.service.impl.MarketPurchaseRequestServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MarketPurchaseRequestApiController {

    private final MarketPurchaseRequestServiceImpl marketPurchaseRequestServiceImpl;

    @PostMapping("/requests")
    public ResponseEntity<String> createMarketPurchaseRequest(@RequestBody MarketPurchaseRequestDto request) {
        marketPurchaseRequestServiceImpl.createMarketPurchaseRequest(request.toEntity(), request.getUserId(), request.getMarketId());
        return ResponseEntity.ok().build();
    }


}
