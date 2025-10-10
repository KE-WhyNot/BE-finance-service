package com.youthfi.finance.global.exception;

import com.youthfi.finance.global.exception.code.BaseCode;
import com.youthfi.finance.global.exception.code.BaseCodeInterface;
import com.youthfi.finance.global.exception.code.status.UserErrorStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.function.Supplier;

@Getter
public class UserException extends RestApiException {

    public UserException(UserErrorStatus errorStatus) {
        super(errorStatus);
    }

    public UserException(UserErrorStatus errorStatus, String message) {
        super(new BaseCodeInterface() {
            @Override
            public BaseCode getCode() {
                return errorStatus.getCode().toBuilder().message(message).build();
            }
        });
    }

    public UserException(UserErrorStatus errorStatus, String message, Throwable cause) {
        super(new BaseCodeInterface() {
            @Override
            public BaseCode getCode() {
                return errorStatus.getCode().toBuilder().message(message).build();
            }
        }, cause);
    }


    // 편의 메서드들


    public static UserException userNotFound(String userId) {
        return new UserException(UserErrorStatus.USER_NOT_FOUND,
            "사용자를 찾을 수 없습니다: " + userId);
    }

    public static UserException invalidAmount(BigDecimal amount) {
        return new UserException(UserErrorStatus.INVALID_AMOUNT,
            "유효하지 않은 금액입니다: " + amount);
    }

    public static UserException insufficientBalance(BigDecimal required, BigDecimal current) {
        return new UserException(UserErrorStatus.INSUFFICIENT_BALANCE,
            "잔액이 부족합니다. 필요: " + required + ", 보유: " + current);
    }

    public static UserException userIdTooLong(String userId) {
        return new UserException(UserErrorStatus.USER_ID_TOO_LONG,
            "사용자 ID는 50자를 초과할 수 없습니다. 입력된 ID: " + userId);
    }

    public static UserException userIdRequired() {
        return new UserException(UserErrorStatus.USER_ID_REQUIRED,
            "사용자 ID는 필수입니다.");
    }

    public static UserException invalidUserId(String message) {
        return new UserException(UserErrorStatus.INVALID_USER_ID, message);
    }


    // 유틸리티 메서드들


    /**
     * 사용자 ID 유효성 검증
     */
    public static void validateUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw userIdRequired();
        }
        if (userId.length() > 50) {
            throw userIdTooLong(userId);
        }
    }

    /**
     * 금액 유효성 검증
     */
    public static void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw invalidAmount(null);
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw invalidAmount(amount);
        }
    }

    /**
     * 잔고 충분성 검증
     */
    public static void validateSufficientBalance(BigDecimal currentBalance, BigDecimal requiredAmount) {
        if (currentBalance.compareTo(requiredAmount) < 0) {
            throw insufficientBalance(requiredAmount, currentBalance);
        }
    }

}
