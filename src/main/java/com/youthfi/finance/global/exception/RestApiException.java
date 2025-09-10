package com.youthfi.finance.global.exception;

import com.youthfi.finance.global.exception.code.BaseCode;
import com.youthfi.finance.global.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RestApiException extends RuntimeException {

    private final BaseCodeInterface errorCode;

    public BaseCode getErrorCode() {
        return this.errorCode.getCode();
    }

}
