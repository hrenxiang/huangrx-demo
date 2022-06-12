package com.huangrx.provider.domain.response;

/**
 * 通用返回对象
 *
 * @author hrenxiang
 * @since 2022-04-24 9:48 PM
 */
public class BaseResponse<T> {

    /**
     * 状态码
     */
    private long code;
    /**
     * 描述信息
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;

    protected BaseResponse() {
    }

    protected BaseResponse(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     * @param  message 提示信息
     */
    public static <T> BaseResponse<T> success(T data, String message) {
        return new BaseResponse<T>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     * @param errorCode 错误码
     */
    public static <T> BaseResponse<T> failed(IErrorCode errorCode) {
        return new BaseResponse<T>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 失败返回结果
     * @param errorCode 错误码
     * @param message 错误信息
     */
    public static <T> BaseResponse<T> failed(IErrorCode errorCode, String message) {
        return new BaseResponse<T>(errorCode.getCode(), message, null);
    }

    /**
     * 失败返回结果
     * @param message 提示信息
     */
    public static <T> BaseResponse<T> failed(String message) {
        return new BaseResponse<T>(ResultCode.FAILED.getCode(), message, null);
    }

    /**
     * 失败返回结果
     */
    public static <T> BaseResponse<T> failed() {
        return failed(ResultCode.FAILED);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> BaseResponse<T> validateFailed() {
        return failed(ResultCode.VALIDATE_FAILED);
    }

    /**
     * 参数验证失败返回结果
     * @param message 提示信息
     */
    public static <T> BaseResponse<T> validateFailed(String message) {
        return new BaseResponse<T>(ResultCode.VALIDATE_FAILED.getCode(), message, null);
    }

    /**
     * 未登录返回结果
     */
    public static <T> BaseResponse<T> unauthorized(T data) {
        return new BaseResponse<T>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * 未授权返回结果
     */
    public static <T> BaseResponse<T> forbidden(T data) {
        return new BaseResponse<T>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), data);
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
