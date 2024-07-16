package com.example.demo.marketpurchaserequest.controller.api;

import com.example.demo.common.filter.dto.marketpurchaserequest.KeywordDto;
import com.example.demo.common.filter.dto.marketpurchaserequest.MarketFilterRequestDto;
import com.example.demo.common.filter.dto.marketpurchaserequest.MarketPurchaseRequestFilterRequestDto;
import com.example.demo.marketpurchaserequest.dto.DetailMarketPurchaseRequestDto;
import com.example.demo.marketpurchaserequest.dto.MarketPurchaseRequestDto;
import com.example.demo.marketpurchaserequest.dto.MarketPurchaseRequestPreviewDto;
import com.example.demo.marketpurchaserequest.dto.MarketResponseDto;
import com.example.demo.marketpurchaserequest.service.MarketPurchaseRequestService;
import java.security.Principal;
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

    @PostMapping()
    public void createMarketPurchaseRequest(@RequestBody MarketPurchaseRequestDto request, Principal principal) {
        String email = principal.getName();
        marketPurchaseRequestService.createMarketPurchaseRequest(request, email);
    }

    @DeleteMapping("/{requestId}")
    public void deleteMarketPurchaseRequest(@PathVariable Long requestId, Principal principal) {
        String email = principal.getName();
        marketPurchaseRequestService.deleteMarketPurchaseRequest(requestId, email);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<DetailMarketPurchaseRequestDto> getMarketPurchaseRequest(@PathVariable Long requestId) {

        return ResponseEntity.ok(marketPurchaseRequestService.getMarketPurchaseRequest(requestId));
    }

    @PostMapping("/list/keyword")
    public ResponseEntity<Page<MarketPurchaseRequestPreviewDto>>
    getMarketPurchaseRequestsByKeyword(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "register") String sort,
            @RequestBody(required = false) KeywordDto keywordDto) {

        Sort sortOrder = marketPurchaseRequestService.confirmSortOrder(sort);
        PageRequest pageRequest = PageRequest.of(page, size, sortOrder);

        var result = marketPurchaseRequestService.getRequestsByKeyword(
                keywordDto, pageRequest);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/list/map")
    public ResponseEntity<Page<MarketPurchaseRequestPreviewDto>>
    getMarketPurchaseRequestsWithinDistance(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "3") double distance,
            Principal principal
    ) {
        var email = principal.getName();
        PageRequest pageRequest = PageRequest.of(page, size);
        var result = marketPurchaseRequestService
                .getRequestsWithinDistance(email, distance, pageRequest);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/list")
    public ResponseEntity<Page<MarketPurchaseRequestPreviewDto>>
    getMarketPurchaseRequestsByFilter(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "register") String sort,
            @RequestBody(required = false) MarketPurchaseRequestFilterRequestDto filterRequestDto) {

        Sort sortOrder = marketPurchaseRequestService.confirmSortOrder(sort);
        PageRequest pageRequest = PageRequest.of(page, size, sortOrder);

        var result = marketPurchaseRequestService
                .getRequestsByFilter(filterRequestDto.getFilter(), pageRequest);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/markets/list")
    public ResponseEntity<Page<MarketResponseDto>>
    getMarketsByFilter(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "name") String sort,
            @RequestBody(required = false) MarketFilterRequestDto filterRequestDto) {

        Sort sortOrder = marketPurchaseRequestService.confirmMarketSortOrder(sort);
        PageRequest pageRequest = PageRequest.of(page, size, sortOrder);

        var result = marketPurchaseRequestService
                .getMarketsByFilter(filterRequestDto.getFilter(), pageRequest);

        return ResponseEntity.ok(result);
    }
}
