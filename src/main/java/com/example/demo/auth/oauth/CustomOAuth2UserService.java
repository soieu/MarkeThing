package com.example.demo.auth.oauth;

import com.example.demo.auth.dto.SignInDto;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.type.AuthType;
import java.util.Objects;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuthRepository oAuthRepository;
    private final PasswordEncoder passwordEncoder;


    public CustomOAuth2UserService(OAuthRepository oAuthRepository, PasswordEncoder passwordEncoder) {
        this.oAuthRepository = oAuthRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("kakao")) {
            System.out.println("kakao");
        } else {
            return null;
        }
        String userEmail = oAuth2Response.getEmail();
        SiteUser existDate = oAuthRepository.findByEmail(userEmail);

        if (Objects.isNull(existDate)) {
            GeometryFactory geometryFactory = new GeometryFactory();
            double longitude = 126.97796919; // 경도
            double latitude = 37.56667062;   // 위도
            Point myLocation = geometryFactory.createPoint(new Coordinate(longitude, latitude));
            String password = "1111";
            SiteUser siteUser = SiteUser.builder()
                    .email(userEmail)
                    .name(oAuth2Response.getName())
                    .password(passwordEncoder.encode(password))
                    .nickname(oAuth2Response.getProviderId())
                    .address("address")
                    .phoneNumber("123")
                    .profileImg("img")
                    .status(true)
                    .authType(AuthType.GOOGLE)
                    .myLocation(myLocation)
                    .build();

            oAuthRepository.save(siteUser);

            SignInDto signInDto = new SignInDto();
            signInDto.setEmail(userEmail);

            return new CustomOAuth2User(signInDto);
        } else {
            existDate.setEmail(oAuth2Response.getEmail());
            existDate.setName(oAuth2Response.getName());

            oAuthRepository.save(existDate);

            SignInDto signInDto = new SignInDto();
            signInDto.setName(oAuth2Response.getName());
            signInDto.setEmail(existDate.getEmail());

            return new CustomOAuth2User(signInDto);
        }

    }
}
