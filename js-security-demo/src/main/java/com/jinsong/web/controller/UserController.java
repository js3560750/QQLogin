package com.jinsong.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.jinsong.dto.User;
import com.jinsong.dto.UserQueryCondition;

@RestController	//声明这是一个支持RESTful API 的控制器
@RequestMapping("/user")	//在类上声明前缀，类中所有方法的url都具有前缀/user，这样方便修改，具体方法的@GetMapping()就不用再写/user
public class UserController {
	
	//用户登录成功后获取用户信息
	//authentication对象会自动存储用户信息，SpringSecurity的默认操作
	//访问http://localhost:8080/user/me ，打开Chrome的检查，Network中查看me对象里的authrities属性
	@GetMapping("/me")
	public Object getCurrentUser(Authentication authentication) {
		return authentication;
	}

	//查询用户
	//@RequestMapping(value="/user",method=RequestMethod.GET)	
	@GetMapping()	//用@GetMapping注解实现上面@RequestMapping(value="/user",method=RequestMethod.GET)这一串功能，使代码更加简单
	@JsonView(User.UserSimpleView.class)	//User类中定义
	public List<User> query(UserQueryCondition condition,	//现在用的是对象查询方法，即把所有的条件都封装到UserQueryCondition这个类中，我们在Http请求中传入的参数名称是UserQueryCondition的成员对象就行
			@PageableDefault(page=1,size=10,sort="username,asc") Pageable pageable){	//Pageable是SpringData里的对象，这样HTTP请求可以传入分页参数,@PageableDefault注解可以设置默认值
		//@RequestParam(required=false)	这样写的话，就是参数不是必须的，没有也可以
		//@RequestParam(name="nickname",required=false,defaultValue="Js") String username
		//这样写的意思是传过来的参数名称是nickname，赋给Java方法中的username，这个参数不是必须的，如果没有的话，默认值是Js
		
		//现在用的是对象查询方法，即把所有的条件都封装到UserQueryCondition这个类中
		//我们在Http请求中传入的参数名称是UserQueryCondition的成员对象就行
		System.out.println(ReflectionToStringBuilder.toString(condition,ToStringStyle.MULTI_LINE_STYLE));	//利用反射的toString工具打印通过condition的传过来的参数
		
		//打印Pageable的分页参数
		System.out.println(pageable.getPageSize());
		System.out.println(pageable.getPageNumber());
		System.out.println(pageable.getSort());
		
		List<User> users =new ArrayList<>();
		users.add(new User());
		users.add(new User());
		users.add(new User());
		return users;
	}
	
	//查询具体用户
	//:\\d+ 冒号后面的是正则表达式，表示这个id参数只能接收数字，不能接收字母或其他符号，如果非数字，会返回4xx状态码
	//@RequestMapping(value="/user/{id:\\d+}",method=RequestMethod.GET)	//"/user/{id}"这里面的id会作为变量传到@PathVariable注解的同名参数下
	@GetMapping("/{id:\\d+}")	//用@GetMapping注解实现上面@RequestMapping的功能，代码更加简单
	@JsonView(User.UserDetailView.class)	//User类中定义
	public User getInfo(@PathVariable String id) {	//@PathVariable的name和required属性和RequestParam是一样的
		System.out.println("进入getInfo方法体");
		User user=new User();
		user.setUsername("jinsong");
		return user;
	}
	
	
	//创建用户
	@PostMapping	//@Valid与BindingResult配合使用，如果校验有错误，错误会放进errors里，方法体里面的内容会执行
	public User create(@Valid @RequestBody User user,BindingResult errors) {	//有了@RequestBody注解，才会解析http传过来的json数据，测试用例whenCreateSuccess()中的content变量
		if(errors.hasErrors()) {	//如果有错误
			errors.getAllErrors().stream().forEach(error->System.out.println(error.getDefaultMessage()));	//打印所有的错误
		}
		System.out.println(user.getId());
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		System.out.println(user.getBirthday());
		
		user.setId("1");
		return user;
	}
	
	//修改用户
	@PutMapping("/{id:\\d+}")
	public User update(@Valid @RequestBody User user,BindingResult errors) {	//@RequestBody用来处理http请求content的内容，绑到相应的bean上
		if(errors.hasErrors()) {	//如果有错误
			errors.getAllErrors().stream().forEach(error->{
				FieldError fieldError=(FieldError)error;	//error强转为fieldError，这样可以打印出具体错误的字段名和错误内容
				String message =fieldError.getField()+" : "+error.getDefaultMessage();
				System.out.println(message);
			}
			);	//打印所有的错误
		}
		System.out.println(user.getId());
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		System.out.println(user.getBirthday());
		
		user.setId("1");
		return user;
	}
	
	//删除具体用户
	@DeleteMapping("/{id:\\d+}")
	public void delete(@PathVariable("id") String id) {	//@PathVariable是用来获得请求url中的动态参数的
		System.out.println(id);
	}
	
	
}
