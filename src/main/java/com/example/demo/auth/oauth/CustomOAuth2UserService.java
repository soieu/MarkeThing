package com.example.demo.auth.oauth;

import com.example.demo.auth.dto.SignInDto;
import com.example.demo.common.kakao.KakaoLocalService;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.AuthType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientKey;

    private final OAuthRepository oAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final SiteUserRepository siteUserRepository;
    private final KakaoLocalService kakaoLocalService;


    public CustomOAuth2UserService(OAuthRepository oAuthRepository, PasswordEncoder passwordEncoder,
            SiteUserRepository siteUserRepository, KakaoLocalService kakaoLocalService) {
        this.oAuthRepository = oAuthRepository;
        this.passwordEncoder = passwordEncoder;
        this.siteUserRepository = siteUserRepository;
        this.kakaoLocalService = kakaoLocalService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("google"))
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

        if (registrationId.equals("kakao")) {
            System.out.println("kakao");
            Map<String, Object> kakaoInfo = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) oAuth2User.getAttributes().get("properties");
            String email = kakaoInfo.get("email").toString();
            String userName = kakaoInfo.get("name").toString();
            double[] coord;
            try {
                coord = kakaoLocalService.getCoord("서울 성동구");
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            GeometryFactory geometryFactory = new GeometryFactory();
            SiteUser findUser = oAuthRepository.findByEmail(email);
            if(Objects.isNull(findUser)) {
                return createKakaoSiteUser(oAuth2User,email);
            } else {
                SignInDto signInDto = new SignInDto();
                signInDto.setName(userName);
                signInDto.setEmail(email);
                return new CustomOAuth2User(signInDto);
            }
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

            existDate.loginEmailName(oAuth2Response.getEmail(),oAuth2Response.getName());

            oAuthRepository.save(existDate);

            SignInDto signInDto = new SignInDto();
            signInDto.setName(oAuth2Response.getName());
            signInDto.setEmail(existDate.getEmail());

            return new CustomOAuth2User(signInDto);
        }
    }

    public CustomOAuth2User createKakaoSiteUser(OAuth2User oAuth2User, String email) {
        Map<String, Object> kakaoInfo = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) oAuth2User.getAttributes().get("properties");
        String userName = kakaoInfo.get("name").toString();
        double[] coord;
        try {
            coord = kakaoLocalService.getCoord("서울 성동구");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

            GeometryFactory geometryFactory = new GeometryFactory();
            KakaoResponse kakaoResponse = KakaoResponse.builder()
                    .nickname(profile.get("nickname").toString())
                    .profileImg(profile.get("profile_image").toString())
                    .userName(userName)
                    .phone("0" + kakaoInfo.get("phone_number").toString().substring(4))
                    .email(email)
                    .address("서울시")
                    .location(geometryFactory.createPoint(new Coordinate(coord[0], coord[1])))
                    .build();
            SiteUser siteUser = kakaoResponse.toEntity(kakaoResponse);
            siteUserRepository.save(siteUser);
            SignInDto signInDto = new SignInDto();
            signInDto.setEmail(email);

            return new CustomOAuth2User(signInDto);
    }
}
