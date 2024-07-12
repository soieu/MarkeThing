package com.example.demo.kakao.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.demo.common.kakao.KakaoLocalService;
import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KakaoLocalServiceTest {

    @Autowired
    private KakaoLocalService kakaoLocalService;

    @Test
    @DisplayName("주소로 위도, 경도 찾기 - 성공")
    void successGetCoord() throws Exception {
        // given
        String address = "서울시 성동구 상원12길";
        // when
        double[] coords = kakaoLocalService.getCoord(address);
        // then
        assertEquals(coords[0],37.5506820354104);
        assertEquals(coords[1],127.049966928991);
    }

    @Test
    @DisplayName("주소로 위도, 경도 찾기 - 실패")
    void failGetCoord() throws Exception {
        // given
        String address = "";
        //when
        MarkethingException markethingException = assertThrows(MarkethingException.class, ()->kakaoLocalService.getCoord(address));
        // then
        assertEquals(markethingException.getErrorCode(), ErrorCode.ADDRESS_CONVERT_FAIL);
    }
    @Test
    @DisplayName("위도, 경도로 주소 찾기 - 성공")
    void successGetAddress() throws Exception {
        // given
        double lat = 37.5506820354104;
        double lon = 127.049966928991;
        // when
        String address = kakaoLocalService.getAddress(lon, lat);
        // then
        assertEquals("서울 성동구 성수동1가 13-219", address);
    }

    @Test
    @DisplayName("위도, 경도로 주소 찾기 - 실패")
    void failGetAddress() throws Exception {
        // given
        double lat = 0;
        double lon = 0;
        // when
        MarkethingException markethingException = assertThrows(MarkethingException.class, ()->kakaoLocalService.getAddress(lat,lon));
        // then
        assertEquals(markethingException.getErrorCode(), ErrorCode.LAT_LON_CONVERT_FAIL);
    }
}
