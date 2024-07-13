package com.example.demo.auth.service;

import com.example.demo.auth.dto.SignupDto;
import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final SiteUserRepository siteUserRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser signUp(SignupDto signupDto) {

        GeometryFactory geometryFactory = new GeometryFactory();
        double longitude = 126.97796919; // 경도
        double latitude = 37.56667062;   // 위도
        Point myLocation = geometryFactory.createPoint(new Coordinate(longitude, latitude));

        boolean isExists = siteUserRepository.existsByEmail(signupDto.getEmail());

        if (isExists) {
            throw new MarkethingException(ErrorCode.EMAIL_ALREADY_EXISTED);
        }

        var result = signupDto.toEntity(passwordEncoder.encode(signupDto.getPassword()),
                myLocation);
        return siteUserRepository.save(result);

    }


}
