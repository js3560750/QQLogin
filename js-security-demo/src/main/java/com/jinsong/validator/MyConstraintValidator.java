package com.jinsong.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

//校验器的逻辑
public class MyConstraintValidator implements ConstraintValidator<MyConstraint, Object>{

	//校验器里可以注入@Autowired Spring容器内的任何对象然后再校验器的方法里使用
	
	@Override	//校验器初始化
	public void initialize(MyConstraint arg0) {
		// TODO Auto-generated method stub
		System.out.println("我的校验器初始化");
	}

	@Override
	public boolean isValid(Object arg0, ConstraintValidatorContext arg1) {
		// TODO Auto-generated method stub
		System.out.println(arg0);
		return true;	//返回true代表校验成功，返回false代表校验失败
	}

}
