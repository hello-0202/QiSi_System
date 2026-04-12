package com.sc.qisi_system.common.result;

import com.sc.qisi_system.common.exception.BusinessException;
import lombok.Data;

@Data
public class Result {

    private Integer code;

    private String message;

    private Object data;

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static Result success() {
        return success(null);
    }

    public static Result error(Integer code, String message) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    public static Result error(BusinessException e) {
        Result result = new Result();
        result.setCode(e.getResultCode().getCode());
        result.setMessage(e.getResultCode().getMessage());
        result.setData(null);
        return result;
    }
}
