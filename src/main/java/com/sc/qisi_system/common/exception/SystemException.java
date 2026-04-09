package com.sc.qisi_system.common.exception;

import com.sc.qisi_system.common.result.ResultCode;
import lombok.Getter;

@Getter
public class SystemException  extends RuntimeException{

    private final ResultCode resultCode;

    public SystemException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public SystemException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }

    public SystemException(ResultCode resultCode, String message, String detail) {
        super(message);
        this.resultCode = resultCode;
    }

    public SystemException(ResultCode resultCode, String message, Throwable cause) {
        super(message, cause);
        this.resultCode = resultCode;
    }
}
