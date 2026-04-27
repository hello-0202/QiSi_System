package com.sc.qisi_system.common.result;

import lombok.Getter;

@Getter
public enum ResultCode {


    /**
     * 状态码
     */
    SUCCESS(0, "success"),
    PARAM_ERROR(4000, "参数错误"),
    REQUEST_BODY_ERROR(4003, "请求体格式错误"),
    NOT_FOUND(4004, "请求的接口不存在"),
    METHOD_NOT_ALLOWED(4005, "请求方法不支持"),
    SYSTEM_ERROR(5000, "服务器繁忙，请稍后重试"),
    PARAM_MISSING(4006, "缺少参数"),
    PARAM_FORMAT_ERROR(4007, "参数格式错误"),

    MINIO_DOWNLOAD_FAILED(500101, "MinIO文件下载失败"),
    MINIO_PRESIGNED_URL_FAILED(500102, "获取MinIO文件临时访问链接失败"),
    FILE_UPLOAD_DB_SAVE_FAILED(500103, "文件上传成功，但数据库保存失败"),


    /**
     * 认证授权
     */
    UNAUTHORIZED(400000, "未登录或认证失败"),
    TOKEN_INVALID(400001, "Token 无效"),
    TOKEN_EXPIRED(400002, "Token 已过期"),
    TOKEN_MALFORMED(400003, "Token 格式错误"),
    TOKEN_SIGNATURE_ERROR(400004, "Token 签名异常"),
    NO_TOKEN(400005, "Token 为空"),
    PERMISSION_DENIED(4000006, "权限不足，无法访问"),
    ACCOUNT_LOGIN_OTHER_DEVICE(400007, "您的账号已在其他设备登录，已强制下线"),
    TOKEN_LOGGED_OUT(400008, "已登出，请重新登录"),


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
    USER_NOT_LOGIN(4001008,"用户未登录"),


    /**
     * 需求模块
     */
    DEMAND_ID_NULL(4002001, "需求ID不能为空"),
    DEMAND_NOT_EXIST(4002002, "需求不存在"),
    DEMAND_STATUS_ERROR(4002003, "需求状态不正确，无法操作"),
    ONLY_ALLOW_DRAFT_EDIT(4002004, "仅草稿状态可修改"),
    ONLY_REVIEWING_CANCEL(4002005,"仅审核中状态可修改"),
    DEMAND_NOT_PUBLISHED(4002006, "需求未发布，无法查看或认领"),
    OPERATE_NOT_ALLOWED(4002007, "无权限操作该需求"),
    DEMAND_TITLE_NULL(4002008, "需求标题不能为空"),
    DEMAND_CONTENT_NULL(4002009, "需求描述不能为空"),
    DEMAND_DEADLINE_ERROR(4002010, "截止时间不能早于当前时间"),
    DEMAND_STATUS_NOT_ALLOW_OPERATE(4002011, "当前状态不允许此操作"),
    DEMAND_AUDIT_ERROR(40020012, "需求审核失败"),
    DEMAND_PUBLISH_ERROR(4002013, "需求发布失败"),
    ATTACHMENT_NOT_FOUND(4002014, "附件不存在"),
    MINIO_DELETE_FAILED(4002015,"文件删除失败"),
    DEMAND_APPLIED_REPEAT(4002016, "你已申请过该需求，请勿重复提交"),
    DEMAND_APPLY_NOT_EXIST(4002017, "该需求申请记录不存在"),
    DEMAND_APPLY_STATUS_NOT_ALLOW(4002018, "当前申请状态不允许操作，仅待审核可撤回"),
    NO_PERMISSION(4002019,"无权限"),
    DEMAND_STATUS_ILLEGAL(4002020, "实践需求状态只能是：已发布、研究中、已完成、已关闭"),
    DEMAND_MEMBER_CHANGE_NOT_EXIST(4002021, "需求成员变更记录不存在"),


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
