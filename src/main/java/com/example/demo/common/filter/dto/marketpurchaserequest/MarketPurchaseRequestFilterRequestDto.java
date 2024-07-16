package com.example.demo.common.filter.dto.marketpurchaserequest;

import com.example.demo.common.filter.dto.community.CommunityFilterDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketPurchaseRequestFilterRequestDto {

    @JsonProperty("filtersForRequest")
    private MarketPurchaseRequestFilterDto filter;
}
