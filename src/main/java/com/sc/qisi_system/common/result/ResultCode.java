package com.sc.qisi_system.common.result;

import lombok.Getter;

@Getter
public enum ResultCode {

    /**
     * 状态码
     */
    SUCCESS(0, "success"),
    PARAM_ERROR(4000, "参数错误"),
    NOT_FOUND(4004, "请求的接口不存在"),
    METHOD_NOT_ALLOWED(4005, "请求方法不支持"),
    SYSTEM_ERROR(5000, "服务器繁忙，请稍后重试"),


    /**
     * 认证授权
     */
    UNAUTHORIZED(400000, "未登录或认证失败"),
    TOKEN_INVALID(400001, "Token 无效"),
    TOKEN_EXPIRED(400002, "Token 已过期"),
    TOKEN_MALFORMED(400003, "Token 格式错误"),
    TOKEN_SIGNATURE_ERROR(400004, "Token 签名异常"),
    PERMISSION_DENIED(4000005, "权限不足，无法访问"),
    ACCOUNT_LOGIN_OTHER_DEVICE(400006, "您的账号已在其他设备登录，已强制下线"),


    /**
     * 用户模块
     */
    USER_NOT_FOUND(4001001, "用户不存在"),
    PASSWORD_ERROR(400102, "用户密码错误"),
    NAME_DUPLICATE(400103, "用户名已存在"),
    USER_LOCKED(4001004, "用户已禁用"),
    PASSWORD_FORMAT_ERROR(400105, "密码格式不正确，请包含字母+数字，长度6-16位"),
    WHITELIST_NOT_FOUND(4001006, "没有查到用户信息，无法注册"),
    USER_TYPE_ERROR(4001007, "用户类型错误"),

    /**
     * 验证码
     */
    CAPTCHA_EMPTY(4001020, "验证码不能为空"),
    CAPTCHA_EXPIRED_OR_CAPTCHA_ERROR(4001021, "验证码已失效或验证码错误"),
    CAPTCHA_REQUEST_FREQUENTLY(4001022, "操作过于频繁，请稍后再试");


    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
