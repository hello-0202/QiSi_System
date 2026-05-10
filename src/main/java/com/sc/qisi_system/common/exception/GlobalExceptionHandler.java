package com.sc.qisi_system.common.exception;

import com.sc.qisi_system.common.result.Result;
import com.sc.qisi_system.common.result.ResultCode;
import com.sc.qisi_system.common.utils.RequestUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.BindingException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
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
        log.warn("[业务异常] {}, code={}, message={}", requestUtils.getRequestLog(request), e.getResultCode(), e.getMessage());
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
        log.warn("[参数异常] {}, code={}, message={}", requestUtils.getRequestLog(request), ResultCode.PARAM_ERROR.getCode(), message);
        return Result.error(ResultCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 方法参数校验异常（@Validated + @RequestParam/@PathVariable 校验失败）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        String message = e.getConstraintViolations().stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse("参数校验失败");
        log.warn("[参数校验异常] {}, code={}, message={}", requestUtils.getRequestLog(request),ResultCode.PARAM_ERROR.getCode(), message);
        return Result.error(ResultCode.PARAM_ERROR.getCode(), message);
    }


    /**
     * 缺少必填请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleMissingParamException(MissingServletRequestParameterException e, HttpServletRequest request) {
        String message = String.format("参数: (%s) %s", e.getParameterType(), e.getParameterName());
        log.warn("[参数缺失异常] {}, code={}, message={}", requestUtils.getRequestLog(request), ResultCode.PARAM_MISSING.getCode(), message);
        return Result.error(ResultCode.PARAM_MISSING.getCode(),ResultCode.PARAM_MISSING.getMessage() + message);
    }


    /**
     * 参数类型转换异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String message = String.format(" 参数: %s", e.getName());
        log.warn("[参数类型转换异常] {}, code={}, message={}",
                requestUtils.getRequestLog(request), ResultCode.PARAM_FORMAT_ERROR.getCode(), ResultCode.PARAM_FORMAT_ERROR.getMessage() + message);
        return Result.error(ResultCode.PARAM_FORMAT_ERROR.getCode(), ResultCode.PARAM_FORMAT_ERROR.getMessage() + message);
    }


    /**
     * 参数绑定异常处理
     * 如：类型转换错误、缺少参数
     */
    @ExceptionHandler(BindingException.class)
    public Result handleBindingException(BindingException e, HttpServletRequest request) {
        log.warn("[参数绑定异常] {}, code={}, message={}", requestUtils.getRequestLog(request), ResultCode.PARAM_ERROR.getCode(), e.getMessage());
        return Result.error(ResultCode.PARAM_ERROR.getCode(), "请求参数格式错误");
    }


    /**
     * 请求体异常处理
     * 如：缺少body、JSON格式错误
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.warn("[请求体异常] {}, code={}, message={}", requestUtils.getRequestLog(request), ResultCode.REQUEST_BODY_ERROR.getCode(), e.getMessage());
        return Result.error(ResultCode.REQUEST_BODY_ERROR.getCode(), ResultCode.REQUEST_BODY_ERROR.getMessage());
    }

    /**
     * 404 接口不存在异常处理
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public Result handleNoResourceFoundException(NoResourceFoundException e, HttpServletRequest request) {
        log.warn("[接口不存在] {}, code={}, message={}", requestUtils.getRequestLog(request), ResultCode.NOT_FOUND.getCode(), e.getMessage());
        return Result.error(ResultCode.NOT_FOUND.getCode(), ResultCode.NOT_FOUND.getMessage());
    }

    /**
     * 请求方法不支持异常处理
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.warn("[方法错误] {}, code={}, message={}", requestUtils.getRequestLog(request), ResultCode.METHOD_NOT_ALLOWED.getCode(), e.getMessage());
        return Result.error(ResultCode.METHOD_NOT_ALLOWED.getCode(), ResultCode.METHOD_NOT_ALLOWED.getMessage());
    }

    /**
     * 数据库访问异常处理
     */
    @ExceptionHandler(DataAccessException.class)
    public Result handleDataAccessException(DataAccessException e, HttpServletRequest request) {
        log.error("[数据库异常] {}, code={}, message={}", requestUtils.getRequestLog(request), ResultCode.SYSTEM_ERROR.getCode(), e.getMessage());
        return Result.error(ResultCode.SYSTEM_ERROR.getCode(), ResultCode.SYSTEM_ERROR.getMessage());
    }

    /**
     * 全局兜底异常处理
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e, HttpServletRequest request) {
        log.error("[系统异常] {}", requestUtils.getRequestLog(request), e);
        return Result.error(ResultCode.SYSTEM_ERROR.getCode(), ResultCode.SYSTEM_ERROR.getMessage());
    }

    /**
     * Redis 连接失败
     */
    @ExceptionHandler(RedisConnectionFailureException.class)
    public Result handleRedisConnectFail(RedisConnectionFailureException e, HttpServletRequest request) {
        log.error("[Redis连接异常] {}, code={}, message={}", requestUtils.getRequestLog(request), ResultCode.SYSTEM_ERROR.getCode(), e.getMessage());
        return Result.error(ResultCode.SYSTEM_ERROR.getCode(), ResultCode.SYSTEM_ERROR.getMessage());
    }


    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result handleAuthorizationDeniedException(AuthorizationDeniedException e, HttpServletRequest request) {
        log.error("[权限异常] {}, code={}, message={}",
                requestUtils.getRequestLog(request),
                ResultCode.PERMISSION_DENIED.getCode(),
                e.getMessage());
        return Result.error(ResultCode.PERMISSION_DENIED.getCode(), ResultCode.PERMISSION_DENIED.getMessage());
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