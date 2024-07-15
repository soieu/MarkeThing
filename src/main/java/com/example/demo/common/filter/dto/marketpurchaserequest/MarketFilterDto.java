package com.example.demo.common.filter.dto.marketpurchaserequest;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MarketFilterDto {

    private String sidoId;
    private List<String> sigunguIds;

    public boolean isAllSidoIncluded() {
        return StringUtils.isBlank(sidoId)
                || "00".equals(sidoId);
    }

    public boolean isAllSigunguIncluded() {
        return sigunguIds.isEmpty()
                || sigunguIds.contains("000");
    }
}

