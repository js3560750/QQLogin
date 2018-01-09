package com.jinsong.web.controller;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringRunner.class)	//spring测试用例要添加的
@SpringBootTest					//spring测试用例要添加的
public class UserControllerTest {

	//↓↓↓↓↓伪造MVC的Web环境，这样不需要启动Tomcat，测试会非常快↓↓↓↓↓
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;	
	
	@Before	//Before注解在所有测试用例运行之前运行
	public void setup() {
		mockMvc= MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	//↑↑↑↑↑↑伪造MVC的Web环境，这样不需要启动Tomcat，测试会非常快↑↑↑↑↑↑
	
	//查询用户信息是否成功的测试用例，看url=/user，与下面区别
	@Test
	public void whenQuerySuccess() throws Exception {
		//mockMvc是一个模拟的Web环境
		//perform是执行的意思，发一个模拟的Get请求，地址是/user
		//contentType定义编码格式
		//perform().andExpect() 是我们发出请求后期望的结果，期望的结果写在andExpect()里面
		//MockMvcResultMatchers.status().isOk()表示返回的状态码是OK的，即状态码=200，如果等于404就是url错误，如果405就是url对的，但是Get/POST这种Method不匹配
		//MockMvcResultMatchers.jsonPath()是返回的JSON内容
		//"$.length()"的意思是我们认为返回的东西是一个集合
		//.value(3)表示集合的值是3，即集合里有3个元素
		String result =mockMvc.perform(MockMvcRequestBuilders.get("/user")	//用url代表资源，用get方法代表查询操作
				.param("username", "jinsong")	//HTTP请求的参数，参数名username，值jinsong
				.param("age", "18")
				.param("size", "15")	//Pageable参数：每页15条数据
				.param("page", "3")		//Pageable参数：查询第三页
				.param("sort", "age,desc")	//Pageable参数：根据age降序排序
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.status().isOk())	//期待返回200状态码
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))//jsonPath可以去git里面搜索这个项目，详细了解怎么用
				.andReturn().getResponse().getContentAsString();	//服务器返回的信息转为String作为返回值，因此有了返回值，这句话不加就没返回值，不影响运行
		
		System.out.println(result);
		
	}
	
	
	//获取具体用户信息是否成功的测试用例，看url=/user/1，与上面区别
	//测试类写的多的时候，想只执行一个测试用例，就右键这个方法，然后Run as Junit Test
	@Test
	public void whenGetInfoSuccess() throws Exception {
		String result=mockMvc.perform(MockMvcRequestBuilders.get("/user/1")
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.username").value("jinsong"))
				.andReturn().getResponse().getContentAsString();
		System.out.println(result);
	}
	
	//当url传入参数是/user/a时，期待返回4xx状态码
	@Test
	public void whenGetInfoFail() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user/a")
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError());	//期待返回的状态码是4xx
	}
	
	//创建用户成功
	@Test
	public void whenCreateSuccess() throws Exception {
		
		
		Date date = new Date();	//前后台时间方面的传递都用时间戳
		System.out.println("Request的时间戳："+date.getTime()); //获得当前时间
		String content="{\"username\":\"jinsong\",\"password\":null,\"birthday\":"+date.getTime()+"}";
		String result=mockMvc.perform(MockMvcRequestBuilders.post("/user")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content))	//这里传的content
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.id").value("1"))	//期待服务器会返回一个id字段，固定值为1
				.andReturn().getResponse().getContentAsString();
		System.out.println("Response："+result);
	}
	
	//修改用户信息，修改用PUT，创建用POST
	@Test
	public void whenUpdateSuccess() throws Exception{
		Date date = new Date();	//前后台时间方面的传递都用时间戳

		System.out.println("Request的时间戳："+date.getTime()); //获得当前时间
		String content="{\"id\":\"1\",\"username\":\"jinsong\",\"password\":null,\"birthday\":"+date.getTime()+"}";
		String result=mockMvc.perform(MockMvcRequestBuilders.put("/user/1")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content))	//这里传的content
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.id").value("1"))	//期待服务器会返回一个id字段，固定值为1
				.andReturn().getResponse().getContentAsString();
		System.out.println("Response："+result);
	}
	
	
	//删除具体用户
	@Test
	public void whenDeleteSuccess()  throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.delete("/user/1")
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	
	//文件上传
	@Test
	public void whenUploadSuccess() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/file")	//模拟一个文件上传的HTTP请求
				.file(new MockMultipartFile("myfile", "test.txt", "multipart/form-data", "upload test".getBytes())))	//第一个参数文件名(与后台FileController中方法接收的参数名要一致)，第二个参数文件原始文件名，第四个参数是文件内容
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	
	
	
	
	
	
}
