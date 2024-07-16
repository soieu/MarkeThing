package com.example.demo.auth.service;

import com.example.demo.auth.dto.PhoneNumberRequestDto;
import com.example.demo.auth.dto.SignUpDto;
import com.example.demo.auth.dto.StringResponseDto;
import com.example.demo.auth.dto.UserInfoDto;
import com.example.demo.common.kakao.KakaoLocalService;
import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final SiteUserRepository siteUserRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    PhoneAuthService phoneAuthService;
    private final KakaoLocalService kakaoLocalService;

    public SiteUser signUp(SignUpDto signupDto)
            throws UnsupportedEncodingException, URISyntaxException {
        double[] temp = kakaoLocalService.getCoord(signupDto.getAddress());
        GeometryFactory geometryFactory = new GeometryFactory();
        double longitude = temp[1]; // 경도
        double latitude = temp[0]; // 위도
        Point myLocation = geometryFactory.createPoint(new Coordinate(longitude, latitude));

        boolean isExists = siteUserRepository.existsByEmail(signupDto.getEmail());

        if (isExists) {
            throw new MarkethingException(ErrorCode.EMAIL_ALREADY_EXISTED);
        }

        var result = signupDto.toEntity(passwordEncoder.encode(signupDto.getPassword()),
                myLocation);
        return siteUserRepository.save(result);

    }

    public SiteUser findId(String phoneNumber){
        return siteUserRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new MarkethingException(ErrorCode.USER_NOT_FOUND));
    }

    public StringResponseDto verifyUser(PhoneNumberRequestDto phoneNumberRequestDto){
        SiteUser siteUser = siteUserRepository.findByEmailAndPhoneNumber(
                        phoneNumberRequestDto.getUserEmail(), phoneNumberRequestDto.getPhoneNumber())
                .orElseThrow(() -> new MarkethingException(ErrorCode.USER_NOT_FOUND));
        return phoneAuthService.sendCode(phoneNumberRequestDto.getPhoneNumber());
    }

    public StringResponseDto updatePassword(Map<String, String> map){
        SiteUser siteUser = siteUserRepository.findByEmail(map.get("email"))
                .orElseThrow(() -> new MarkethingException(ErrorCode.USER_NOT_FOUND));
        siteUser.updatePassword(passwordEncoder.encode(map.get("password")));
        siteUserRepository.save(siteUser);
        return new StringResponseDto("비밀번호 변경이 완료되었습니다.");
    }

    public SiteUser findUser(Long userId){
        SiteUser siteUser = siteUserRepository.findById(userId)
                .orElseThrow(() -> new MarkethingException(ErrorCode.USER_NOT_FOUND));
        UserInfoDto userInfo = new UserInfoDto();
        return userInfo.toEntity(siteUser.getNickname(), siteUser.getMannerScore(),
                siteUser.getProfileImg());
    }


}
