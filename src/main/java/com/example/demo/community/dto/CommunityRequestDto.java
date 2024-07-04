package com.example.demo.community.dto;

import com.example.demo.community.entity.Community;
import com.example.demo.siteuser.entity.SiteUser;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommunityRequestDto {

    @NotBlank(message = "지역명을 입력해 주세요.")
    private String area;

    @NotBlank(message = "타이틀을 입력해 주세요.")
    private String title;

    @NotBlank(message = "게시글 내용을 입력해 주세요.")
    private String content;

    private String postImg;

    public Community toEntity(SiteUser siteUser) {
        return Community.builder()
                .siteUser(siteUser)
                .area(area)
                .title(title)
                .content(content)
                .postImg(postImg)
                .build();
    }
}
