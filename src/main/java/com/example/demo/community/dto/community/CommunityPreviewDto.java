package com.example.demo.community.dto.community;

import com.example.demo.community.entity.Community;
import com.example.demo.community.type.AreaType;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommunityPreviewDto {

    private long id;

    private String nickname;

    @NotBlank(message = "지역명을 입력해 주세요.")
    private AreaType area;

    @NotBlank(message = "타이틀을 입력해 주세요.")
    private String title;

    public static CommunityPreviewDto fromEntity(Community community) {
        return CommunityPreviewDto.builder()
                .id(community.getId())
                .nickname(community.getSiteUser().getNickname())
                .area(community.getArea())
                .title(community.getTitle())
                .build();
    }
}
