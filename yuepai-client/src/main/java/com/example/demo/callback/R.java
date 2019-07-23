package com.example.demo.callback;

public class R<D> {
    public static final int CODE_SUCCESS = 200;//访问成功
    public static final int CODE_FAIL = 500;//访问失败
    public static final int CODE_LOGIN_FAIL = 501;//登陆失效
    public static final int CODE_LOGIN_ACCOUNT = 502;//账号不存在
    public static final int CODE_WRONG_PWD = 503;//密码错误
    public static final int CODE_FROZEN = 504;//账号冻结
    public static final int CODE_TOKEN_FAIL = 505;//token失效
    public static final int CODE_ACCOUNT_COVER = 506;//账号重复

    private boolean ret;
    private int code;
    private D data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRet() {
        return ret;
    }

    public void setRet(boolean ret) {
        this.ret = ret;
    }

    public R(boolean ret, int code, D data, String message) {
        this.ret = ret;
        this.code = code;
        this.data = data;
        this.message = message;
    }
}
