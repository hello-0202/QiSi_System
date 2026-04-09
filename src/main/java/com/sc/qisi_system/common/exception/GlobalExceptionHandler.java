package com.sc.qisi_system.common.exception;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.common.utils.RequestUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.BindingException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全局统一异常处理器
 * 捕获所有Controller层抛出的异常，统一返回格式、统一日志打印
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final RequestUtils requestUtils;


    /**
     * 自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("[业务异常] {},code={}，message={}", requestUtils.getRequestLog(request), e.getResultCode(), e.getMessage());
        return Result.error(e.getResultCode().getCode(), e.getMessage());
    }

    /**
     * 自定义系统异常
     */
    @ExceptionHandler(SystemException.class)
    public Result handleSystemException(SystemException e, HttpServletRequest request) {
        log.warn("[系统异常] {}, code={}, message={}", requestUtils.getRequestLog(request), e.getResultCode().getCode(), e.getMessage());
        return Result.error(e.getResultCode().getCode(), e.getMessage());
    }

    /**
     * 参数校验异常处理: @Valid 校验失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("参数错误");
        log.warn("[参数异常] {}，提示：{}", requestUtils.getRequestLog(request), message);
        return Result.error(ResultCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 参数绑定异常处理
     * 如：类型转换错误、缺少参数
     */
    @ExceptionHandler(BindingException.class)
    public Result handleBindingException(BindingException e, HttpServletRequest request) {
        log.warn("[绑定异常] {}，异常：{}", requestUtils.getRequestLog(request), e.getMessage());
        return Result.error(ResultCode.PARAM_ERROR.getCode(), "请求参数格式错误");
    }

    /**
     * 请求体异常处理
     * 如：缺少body、JSON格式错误
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.warn("[请求体异常] {}，异常：{}", requestUtils.getRequestLog(request), e.getMessage());
        return Result.error(ResultCode.PARAM_ERROR.getCode(), ResultCode.PARAM_ERROR.getMessage());
    }

    /**
     * 404 接口不存在异常处理
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public Result handleNoResourceFoundException(NoResourceFoundException e, HttpServletRequest request) {
        log.warn("[接口不存在] {}，异常：{}", requestUtils.getRequestLog(request), e.getMessage());
        return Result.error(ResultCode.NOT_FOUND.getCode(), ResultCode.NOT_FOUND.getMessage());
    }

    /**
     * 请求方法不支持异常处理
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.warn("[方法错误] {}，异常：{}", requestUtils.getRequestLog(request), e.getMessage());
        return Result.error(ResultCode.METHOD_NOT_ALLOWED.getCode(), ResultCode.METHOD_NOT_ALLOWED.getMessage());
    }

    /**
     * 数据库访问异常处理
     */
    @ExceptionHandler(DataAccessException.class)
    public Result handleDataAccessException(DataAccessException e, HttpServletRequest request) {
        log.error("[数据库异常] {}，异常：{}", requestUtils.getRequestLog(request), e.getMessage());
        return Result.error(ResultCode.SYSTEM_ERROR.getCode(), ResultCode.SYSTEM_ERROR.getMessage());
    }

    /**
     * 全局兜底异常处理
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e, HttpServletRequest request) {
        log.error("[系统异常] {}，异常：{}", requestUtils.getRequestLog(request), e.getMessage());
        return Result.error(ResultCode.SYSTEM_ERROR.getCode(), ResultCode.SYSTEM_ERROR.getMessage());
    }

    /**
     *  Redis 连接失败
     */
    @ExceptionHandler(RedisConnectionFailureException.class)
    public Result handleRedisConnectFail(RedisConnectionFailureException e, HttpServletRequest request) {
        log.error("[Redis连接异常] {}，异常={}", requestUtils.getRequestLog(request), e.getMessage());
        return Result.error(ResultCode.SYSTEM_ERROR.getCode(), ResultCode.SYSTEM_ERROR.getMessage());
    }

    /**
     * JWT Token 过期异常处理
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public Result handleExpiredJwtException(ExpiredJwtException e, HttpServletRequest request) {
        log.warn("[Token过期] {}，异常：{}", requestUtils.getRequestLog(request), e.getMessage());
        return Result.error(ResultCode.TOKEN_EXPIRED.getCode(), e.getMessage());
    }

    /**
     * JWT 签名错误异常处理
     */
    @ExceptionHandler(SignatureException.class)
    public Result handleSignatureException(SignatureException e, HttpServletRequest request) {
        log.warn("[Token签名错误] {}，异常：{}", requestUtils.getRequestLog(request), e.getMessage());
        return Result.error(ResultCode.TOKEN_SIGNATURE_ERROR.getCode(), e.getMessage());
    }

    /**
     * JWT 格式错误/非法Token异常处理
     */
    @ExceptionHandler(MalformedJwtException.class)
    public Result handleMalformedJwtException(MalformedJwtException e, HttpServletRequest request) {
        log.warn("[Token格式错误] {}，异常：{}", requestUtils.getRequestLog(request), e.getMessage());
        return Result.error(ResultCode.TOKEN_MALFORMED.getCode(), e.getMessage());
    }

}