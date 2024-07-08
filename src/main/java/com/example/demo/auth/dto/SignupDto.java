package com.example.demo.auth.dto;


import com.example.demo.community.entity.Community;
import com.example.demo.siteuser.entity.SiteUser;
import lombok.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto {

  private final GeometryFactory geometryFactory = new GeometryFactory();

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



  private String ProfileImg;

  public SiteUser toEntity(String password) {

    return SiteUser.builder()
      .email(email)
      .password(password)
      .name(name)
      .nickname(nickname)
      .phoneNumber(phoneNumber)
      .address(address)
      .profileImg(ProfileImg)
      .myLocation(geometryFactory.createPoint(new Coordinate(80.97796919, 90.56667062)))
      .build();
  }


}
