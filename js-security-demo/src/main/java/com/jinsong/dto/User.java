package com.jinsong.dto;

import java.util.Date;

import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonView;

public class User {

	private String id;
	private String username;
	@NotBlank(message="密码不能为空")	//只要用到了User类，并且添加了@Valid，那么password不能为null，看UserController的create方法
	private String password;
	@Past(message="生日必须是过去的时间")	//birthday必须是一个过去的时间,message属性定义错误消息，不定义也有默认的错误消息
	private Date birthday;
	
	public interface UserSimpleView	{};	//UserController中的@JSonView要用到，不显示用户密码
	public interface UserDetailView	extends UserSimpleView{};	//UserController中的@JSonView要用到，显示用户密码,继承的作用是，显示用户详细视图的时候，也会显示用户简单视图的所有信息
	
	@JsonView(UserSimpleView.class)	//只展示UserSimpleView
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@JsonView(UserDetailView.class)	//通过继承关系，在展示UserDetailView的时候也会展示UserSimpleView
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@JsonView(UserSimpleView.class)	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@JsonView(UserSimpleView.class)	
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
}
