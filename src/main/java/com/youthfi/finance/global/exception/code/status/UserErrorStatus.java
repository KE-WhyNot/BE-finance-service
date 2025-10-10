package com.youthfi.finance.global.exception.code.status;

import com.youthfi.finance.global.exception.code.BaseCode;
import com.youthfi.finance.global.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorStatus implements BaseCodeInterface {

    // User 관련 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4001", "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER4002", "이미 존재하는 사용자입니다."),
    INVALID_USER_ID(HttpStatus.BAD_REQUEST, "USER4003", "유효하지 않은 사용자 ID입니다."),
    INVALID_AMOUNT(HttpStatus.BAD_REQUEST, "USER4004", "유효하지 않은 금액입니다."),
    INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "USER4005", "잔액이 부족합니다."),
    USER_ID_TOO_LONG(HttpStatus.BAD_REQUEST, "USER4006", "사용자 ID는 50자를 초과할 수 없습니다."),
    USER_ID_REQUIRED(HttpStatus.BAD_REQUEST, "USER4007", "사용자 ID는 필수입니다."),
    BALANCE_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "USER5001", "잔고 업데이트에 실패했습니다."),
    USER_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "USER5002", "사용자 생성에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final boolean isSuccess = false;
    private final String code;
    private final String message;

    @Override
    public BaseCode getCode() {
        return BaseCode.builder()
                .httpStatus(httpStatus)
                .isSuccess(isSuccess)
                .code(code)
                .message(message)
                .build();
    }
}
