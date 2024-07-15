package com.example.demo.common.filter.dto.community;

import com.example.demo.community.type.AreaType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommunityFilterDto {
    private List<AreaType> areas;
}
