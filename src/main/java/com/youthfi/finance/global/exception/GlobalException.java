package com.youthfi.finance.global.exception;

import com.youthfi.finance.global.exception.code.BaseCode;
import com.youthfi.finance.global.exception.code.BaseCodeInterface;
import com.youthfi.finance.global.exception.code.status.GlobalErrorStatus;
import lombok.Getter;

@Getter
public class GlobalException extends RestApiException {

    public GlobalException(GlobalErrorStatus errorStatus) {
        super(errorStatus);
    }

    public GlobalException(GlobalErrorStatus errorStatus, String message) {
        super(new BaseCodeInterface() {
            @Override
            public BaseCode getCode() {
                return errorStatus.getCode().toBuilder().message(message).build();
            }
        });
    }

    public GlobalException(GlobalErrorStatus errorStatus, String message, Throwable cause) {
        super(new BaseCodeInterface() {
            @Override
            public BaseCode getCode() {
                return errorStatus.getCode().toBuilder().message(message).build();
            }
        }, cause);
    }

    // ===========================================
    // 인증/보안 관련 편의 메서드
    // ===========================================

    public static GlobalException authenticationProcessingError(Throwable cause) {
        return new GlobalException(GlobalErrorStatus.AUTHENTICATION_PROCESSING_ERROR, 
            "인증 처리 중 오류가 발생했습니다.", cause);
    }

    public static GlobalException authenticatedUserNotFound() {
        return new GlobalException(GlobalErrorStatus.AUTHENTICATED_USER_NOT_FOUND, 
            "인증된 사용자를 찾을 수 없습니다.");
    }

    public static GlobalException userIdNotFound() {
        return new GlobalException(GlobalErrorStatus.USER_ID_NOT_FOUND, 
            "사용자 ID를 찾을 수 없습니다.");
    }

    public static GlobalException accessDenied() {
        return new GlobalException(GlobalErrorStatus.ACCESS_DENIED, 
            "접근 권한이 없습니다.");
    }
}
