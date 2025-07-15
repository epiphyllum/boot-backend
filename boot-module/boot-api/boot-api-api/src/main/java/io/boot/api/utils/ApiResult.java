/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.api.utils;

import io.boot.commons.tools.utils.MessageUtils;
import lombok.Data;

import java.io.Serializable;

@Data
public class ApiResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public static ApiResult success = new ApiResult();
    private int code = 0;
    private String msg = "success";
    private T data;

    public ApiResult<T> ok(T data) {
        this.setData(data);
        return this;
    }

    public boolean success() {
        return code == 0;
    }

    public ApiResult<T> error() {
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = MessageUtils.getMessage(this.code);
        return this;
    }

    public ApiResult<T> error(int code) {
        this.code = code;
        this.msg = MessageUtils.getMessage(this.code);
        return this;
    }

    public ApiResult<T> error(int code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public ApiResult<T> error(String msg) {
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
