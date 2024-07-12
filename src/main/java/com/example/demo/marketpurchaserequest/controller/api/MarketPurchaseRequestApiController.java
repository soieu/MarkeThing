package com.example.demo.marketpurchaserequest.controller.api;

import com.example.demo.common.filter.dto.KeywordDto;
import com.example.demo.marketpurchaserequest.dto.DetailMarketPurchaseRequestDto;
import com.example.demo.marketpurchaserequest.dto.MarketPurchaseRequestDto;
import com.example.demo.marketpurchaserequest.dto.MarketPurchaseRequestPreviewDto;
import com.example.demo.marketpurchaserequest.service.MarketPurchaseRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/requests")
public class MarketPurchaseRequestApiController {

    private final MarketPurchaseRequestService marketPurchaseRequestService;

    @PostMapping
    public void createMarketPurchaseRequest(@RequestBody MarketPurchaseRequestDto request) {
        marketPurchaseRequestService.createMarketPurchaseRequest(request);
    }

    @DeleteMapping("/{requestId}")
    public void deleteMarketPurchaseRequest(@PathVariable Long requestId) {
        marketPurchaseRequestService.deleteMarketPurchaseRequest(requestId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<DetailMarketPurchaseRequestDto> getMarketPurchaseRequest(@PathVariable Long requestId) {

        return ResponseEntity.ok(marketPurchaseRequestService.getMarketPurchaseRequest(requestId));
    }

    @PostMapping("/list/keyword")
    public ResponseEntity<Page<MarketPurchaseRequestPreviewDto>> getMarketPurchaseRequestList(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "register") String sort,
            @RequestBody(required = false) KeywordDto keywordDto) {

        Sort sortOrder = marketPurchaseRequestService.confirmSortOrder(sort);
        PageRequest pageRequest = PageRequest.of(page, size, sortOrder);

        var result = marketPurchaseRequestService.getRequestByKeyword(
                keywordDto, pageRequest);

        return ResponseEntity.ok(result);

    }
}
