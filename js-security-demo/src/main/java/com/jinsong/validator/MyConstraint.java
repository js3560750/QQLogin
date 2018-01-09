package com.jinsong.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

//这是一个我们自己定义的校验注解，使用的时候添加@MyConstraint 
@Target({ElementType.METHOD,ElementType.FIELD})	//定义这个注解可以标注在方法和字段上
@Retention(RetentionPolicy.RUNTIME)	//定义这是一个运行时的注解
@Constraint(validatedBy=MyConstraintValidator.class)	//定义校验器逻辑
public @interface MyConstraint {	//注意@interface前的@
	
	//下面三个是必须要添加的，加上就行了
	String message();

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
