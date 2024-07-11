package com.example.demo.exception.type;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // NOT_FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"사용자를 찾을 수 없습니다."),
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "주소가 존재하지 않습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"이메일을 찾을 수 없습니다."),
    LAT_AND_LON_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "위경도를 찾을 수 없는 주소입니다."),
    REGISTRATION_INFO_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "가입 정보가 없습니다."),
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 채팅방입니다."),
    COMMUNITY_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"커뮤니티를 찾을 수 없습니다."),
    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 의뢰글 입니다,"),
    MARKET_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"시장을 찾을 수 없습니다."),

    // UNAUTHORIZED
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED.value(), "로그인이 필요한 서비스입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "엑세스 토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 엑세스 토큰입니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED.value(), "인가되지 않은 회원입니다."),

    // BAD_REQUEST
    INVALID_SESSION(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 세션입니다."),
    EMAIL_ALREADY_EXISTED(HttpStatus.BAD_REQUEST.value(), "이미 사용 중인 이메일입니다."),
    NICKNAME_ALREADY_EXISTED(HttpStatus.BAD_REQUEST.value(), "이미 사용 중인 닉네임입니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다."),
    JSON_PARSING_FAILED(HttpStatus.BAD_REQUEST.value(), "JsonParse 과정에서 예외가 발생하였습니다."),
    NOTIFICATION_CONNECTION_FAILED(HttpStatus.BAD_REQUEST.value(), "알림 연결에 실패하였습니다."),
    S3_UPLOAD_FAILED(HttpStatus.BAD_REQUEST.value(), "파일 업로드가 실패했습니다."),
    KAKAO_ACCESS_TOKEN_FAIL(HttpStatus.BAD_REQUEST.value(),"카카오 엑세스 토큰 발급에 실패했습니다."),
    KAKAO_USER_INFO_FAIL(HttpStatus.BAD_REQUEST.value(),"카카오 사용자 정보 조회에 실패했습니다."),
    PHONE_AUTH_NUM_EXPIRED(HttpStatus.BAD_REQUEST.value(), "휴대폰 인증 번호가 만료되었습니다."),
    PHONE_AUTH_NUM_DOESNT_MATCH(HttpStatus.BAD_REQUEST.value(), "휴대폰 인증 번호가 일치하지 않습니다."),
    SUSPECT_PAYMENT_FORGERY(HttpStatus.BAD_REQUEST.value(), "결제 위변조가 의심됩니다."),
    ORDER_NOT_EXIST(HttpStatus.BAD_REQUEST.value(), "없는 주문정보입니다."),
    PAYMENT_INCOMPLETE(HttpStatus.BAD_REQUEST.value(), "결제가 완료되지 않았습니다."),
    PAYMENT_CANCEL_INCOMPLETE(HttpStatus.BAD_REQUEST.value(), "결제취소가 완료되지 않았습니다."),
    PAYMENT_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "없는 결제정보입니다."),


    // INTERNAL_SERVER_ERROR
    SMS_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "sms 문자 전송에 실패했습니다."),
    IAMPORT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "아임포트 API 호출을 실패했습니다."),
    ;

    private final int code;
    private final String description;
}
