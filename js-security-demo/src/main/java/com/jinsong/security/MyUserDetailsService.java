/**
 * 
 */
package com.jinsong.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

/**
 * @author 188949420@qq.com
 *
 */
@Component	//组件别掉了，不然Springboot识别不了
public class MyUserDetailsService implements UserDetailsService,SocialUserDetailsService{
	
	private Logger logger =LoggerFactory.getLogger(getClass());
	
	//PasswordEncoder这个接口有两个方法，一个是encode()，负责加密，一个是matches()，负责验证
	//这里的PasswordEncoder用org.springframework.security.crypto.这个包下的
	@Autowired
	private PasswordEncoder passwordEncoder;

	//账户校验
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("表单登录用户名"+username);
		//这里应该根据用户名从数据库查询密码
		//@Autowired Mybaits 然后通过Mybatis的逻辑拿到数据库的密码
		//这里我们开启了加密，加密的配置在BrowserSecurityConfig.java中
		//所以这个password是加密后的密码，这里是为了演示，实际项目中，应该在用户注册的时候调用这个passwordEncoder.encode()方法，然后把加密后的密码存入数据库
		//即实际项目中，数据库存的是加密后的密码，这里仍然是从数据库中拿密码。
		String password =passwordEncoder.encode("123456");	//假设这个password是我们从数据库中查询的
		logger.info("数据库存的加密之后的密码是："+password);
		
		boolean accountNonLocked;
		//根据数据库查找的信息判断账户是否被冻结，这里我们写死了。
		if(true) {
			accountNonLocked=true;
		}else {
			accountNonLocked=false;
		}
		
		
		//返回的这个User是 org.springframework.security包里的，实现了UserDetails接口。第一个参数是用户输入的用户名，第二个参数是根据这个用户名在数据库中查询得到的密码，最后一个参数是权限，这里我们暂且自定义admin权限
		//SpringSecurity会拿这个数据库的password和用户输入的密码自动进行比对，比对不一致的话就无法登陆
		return new User(username, 
				password, 
				true, 	//这几个参数我们写死了，其实这几个参数我们可以写验证逻辑，如果true就验证通过，如果传入的false验证就不能通过，需要我们自己去实现。
				true,	//根据形参名字，第一个true的位置应该是账户是否删除，第二个账户是否过期，第三个是密码是否过期，第四个accountNonLocked是账户是否冻结
				true,
				accountNonLocked,
				AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
	}

	//第三方登录用的
	//这里是QQ登录，userId就是数据库my_userconnection表中的userId
	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
		logger.info("第三方登录用户ID"+userId);
		//这里应该根据用户名从数据库查询密码
		//@Autowired Mybaits 然后通过Mybatis的逻辑拿到数据库的密码
		//这里我们开启了加密，加密的配置在BrowserSecurityConfig.java中
		//所以这个password是加密后的密码，这里是为了演示，实际项目中，应该在用户注册的时候调用这个passwordEncoder.encode()方法，然后把加密后的密码存入数据库
		//即实际项目中，数据库存的是加密后的密码，这里仍然是从数据库中拿密码。
		String password =passwordEncoder.encode("123456");	//假设这个password是我们从数据库中查询的
		logger.info("数据库存的加密之后的密码是："+password);
		
		return new SocialUser(userId, password, true,true,true,true,AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
	}

}
