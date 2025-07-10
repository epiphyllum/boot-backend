/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.commons.tools.exception;


import io.boot.commons.tools.utils.MessageUtils;

/**
 * 自定义异常
 *
 * @author epiphyllum.zhou@gmail.com
 * @since 1.0.0
 */
public class BootException extends RuntimeException {
	private static final long serialVersionUID = 1L;

    private int code;
	private String msg;

	public BootException(int code) {
		this.code = code;
		this.msg = MessageUtils.getMessage(code);
	}

	public BootException(int code, String... params) {
		this.code = code;
		this.msg = MessageUtils.getMessage(code, params);
	}

	public BootException(int code, Throwable e) {
		super(e);
		this.code = code;
		this.msg = MessageUtils.getMessage(code);
	}

	public BootException(int code, Throwable e, String... params) {
		super(e);
		this.code = code;
		this.msg = MessageUtils.getMessage(code, params);
	}

	public BootException(String msg) {
		super(msg);
		this.code = ErrorCode.INTERNAL_SERVER_ERROR;
		this.msg = msg;
	}

	public BootException(String msg, Throwable e) {
		super(msg, e);
		this.code = ErrorCode.INTERNAL_SERVER_ERROR;
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}