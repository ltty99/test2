package com.tedu.web.util;

public class JsonResult<T>
{
    public static final int SUCCESS = 200;
    public static final int NOT_LOGIN = 400;
    public static final int EXCEPTION = 401;
    public static final int SYS_ERROR = 402;
    public static final int PARAMS_ERROR = 403;
    public static final int NOT_SUPPORTED = 410;
    public static final int INVALID_AUTHCODE = 444;
    public static final int TOO_FREQUENT = 445;
    public static final int UNKNOWN_ERROR = 499;
    private int code;
    private String msg;
    private T data;
    
    public static JsonResult build() {
        return new JsonResult();
    }
    
    public static JsonResult build(final int code) {
        return new JsonResult().code(code);
    }
    
    public static JsonResult build(final int code, final String msg) {
        return new JsonResult().code(code).msg(msg);
    }
    
    public static <T> JsonResult<T> build(final int code, final T data) {
        return new JsonResult<T>().code(code).data(data);
    }
    
    public static <T> JsonResult<T> build(final int code, final String msg, final T data) {
        return new JsonResult<T>().code(code).msg(msg).data(data);
    }
    
    public JsonResult<T> code(final int code) {
        this.code = code;
        return this;
    }
    
    public JsonResult<T> msg(final String msg) {
        this.msg = msg;
        return this;
    }
    
    public JsonResult<T> data(final T data) {
        this.data = data;
        return this;
    }
    
    public static JsonResult ok() {
        return build(200);
    }
    
    public static JsonResult ok(final String msg) {
        return build(200, msg);
    }
    
    public static <T> JsonResult<T> ok(final T data) {
        return build(200, data);
    }
    
    public static JsonResult err() {
        return build(401);
    }
    
    public static JsonResult err(final String msg) {
        return build(401, msg);
    }
    
    @Override
    public String toString() {
        return JsonUtil.to(this);
    }
    
    public int getCode() {
        return this.code;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public T getData() {
        return this.data;
    }
    
    public void setCode(final int code) {
        this.code = code;
    }
    
    public void setMsg(final String msg) {
        this.msg = msg;
    }
    
    public void setData(final T data) {
        this.data = data;
    }
}
