package com.youthfi.finance.global.exception;

import com.youthfi.finance.global.exception.code.BaseCode;
import com.youthfi.finance.global.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;

public class RestApiException extends RuntimeException {

    private final BaseCodeInterface errorCode;

    public RestApiException(BaseCodeInterface errorCode) {
        super(errorCode.getCode().getMessage());
        this.errorCode = errorCode;
    }

    public RestApiException(BaseCodeInterface errorCode, Throwable cause) {
        super(errorCode.getCode().getMessage(), cause);
        this.errorCode = errorCode;
    }

    public BaseCode getErrorCode() {
        return this.errorCode.getCode();
    }

}
