package com.jinsong.core.validate.code;

import org.springframework.security.core.AuthenticationException;

/**
 * 自定义的验证码校验异常
 * 
 * 
 * @author 188949420@qq.com
 *
 */
public class ValidateCodeException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1436799412429645677L;

	public ValidateCodeException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

}
