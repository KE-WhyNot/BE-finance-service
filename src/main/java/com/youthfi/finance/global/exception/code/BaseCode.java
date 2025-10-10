package com.youthfi.finance.global.exception.code;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Objects;

@Getter
@Builder(toBuilder = true)
public class BaseCode {

    private final HttpStatus httpStatus;
    private final boolean isSuccess;
    private final String code;
    private final String message;

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        BaseCode baseCode = (BaseCode) object;
        return isSuccess == baseCode.isSuccess && httpStatus == baseCode.httpStatus && Objects.equals(code, baseCode.code) && Objects.equals(message, baseCode.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpStatus, isSuccess, code, message);
    }

    @Override
    public String toString() {
        return "BaseCode{" +
                "httpStatus=" + httpStatus +
                ", isSuccess=" + isSuccess +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

}
