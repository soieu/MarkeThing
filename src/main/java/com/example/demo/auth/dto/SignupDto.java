package com.example.demo.auth.dto;


import com.example.demo.siteuser.entity.SiteUser;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;


@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class SignupDto {

    @NotBlank(message = "이메일을 입력해 주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;

    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;

    @NotBlank(message = "닉네임을 입력해 주세요.")
    private String nickname;

    @NotBlank(message = "전화번호를 입력해 주세요.")
    private String phoneNumber;

    @NotBlank(message = "주소를 입력해 주세요.")
    private String address;

    private Point myLocation;

    private String ProfileImg;

    public SiteUser toEntity(String password, Point myLocation) {
        return SiteUser.builder()
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .address(address)
                .profileImg(ProfileImg)
                .myLocation(myLocation)
                .build();
    }


}
