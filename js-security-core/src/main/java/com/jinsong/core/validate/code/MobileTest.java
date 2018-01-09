package com.jinsong.core.validate.code;

/**
 * 为了让ValidateCodeController的createSmsCode返回JSON格式数据，特意建的测试类
 * 
 * Springboot中，经过测试，加不加@ResponseBody 只要return的是一个类，就会返回这个类的JSON格式数据
 * 
 * 比如return MobileTest 这个类
 * 就会返回{"mobile":"18571686931","code":"316375"}这样的JSON格式
 * 
 * @author 188949420@qq.com
 *
 */
public class MobileTest {

	private String mobile;
	private String code;
	
	
	public MobileTest(String mobile, String code) {
		super();
		this.mobile = mobile;
		this.code = code;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
