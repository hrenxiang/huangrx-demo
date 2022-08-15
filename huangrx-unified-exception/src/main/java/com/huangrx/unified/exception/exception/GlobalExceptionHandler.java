package com.huangrx.unified.exception.exception;

import com.huangrx.unified.exception.domain.BaseResponse;
import com.huangrx.unified.exception.domain.IErrorCode;
import com.huangrx.unified.exception.domain.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * 全局异常拦截
 *
 * @author jason
 */
@Slf4j
@RestControllerAdvice(basePackages = {
        "com.huangrx.unified.exception.controller"
})
public class GlobalExceptionHandler {

    /**
     * Validation校验失败异常封装响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private BaseResponse<Object> handlerErrorInfo(HttpServletRequest request, MethodArgumentNotValidException e) {
        log.error("URI：{}，参数校验异常：{}", request.getRequestURI(), e.getMessage());
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        ObjectError objectError = errorList.get(0);
        String failMessage = objectError.getDefaultMessage();
        log.info(failMessage);
        // 将校验失败结果，封装到统一的响应
        return BaseResponse.failed(ResultCode.VALIDATE_FAILED, failMessage);
    }

    /**
     * Asserts 异常处理
     */
    @ExceptionHandler(IllegalArgumentException.class)
    private BaseResponse<Object> handlerIllegalArgumentException(HttpServletRequest request, Exception e) {
        log.error("URI：{}，非法参数异常", request.getRequestURI(), e);
        return BaseResponse.failed(ResultCode.SERVER_ERROR, e.getMessage());
    }

    /**
     * Http请求消息序列化异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    private BaseResponse<Object> handlerErrorInfo(HttpServletRequest request, HttpMessageNotReadableException e) {
        log.error("URI：{}，Http请求消息序列化异常", request.getRequestURI(), e);
        return BaseResponse.failed(ResultCode.PARAMS_FAILED);
    }

    /**
     * 自定义API异常封装响应
     */
    @ExceptionHandler(ApiException.class)
    private BaseResponse<Object> handlerErrorInfo(HttpServletRequest request, ApiException e) {
        log.error("URI：{}，自定义API异常", request.getRequestURI(), e);
        if (Objects.isNull(e.getErrorCode())) {
            return BaseResponse.failed(e.getMessage());
        }
        return BaseResponse.failed(e.getErrorCode());
    }


    /**
     * 全局异常封装响应
     */
    @ExceptionHandler(Exception.class)
    private BaseResponse<Object> handlerErrorInfo(HttpServletRequest request, Exception e) {
        log.error("URI：{}，全局异常", request.getRequestURI(), e);
        return BaseResponse.failed(ResultCode.ADMIN_ERROR);
    }

}