package com.jinsong.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jinsong.dto.FileInfo;

@RestController
@RequestMapping("/file")
public class FileController {
	
	//保存的路径
	String folder="C:/Users/18894/Desktop/Design/JAVA/js-security-demo/src/main/java/com/jinsong/web/controller";

	//上传文件服务，对应的单元测试用例在UserControllerTest中
	@PostMapping
	public FileInfo upload(MultipartFile myfile) throws Exception {
		System.out.println(myfile.getName());
		System.out.println(myfile.getOriginalFilename());
		System.out.println(myfile.getSize());
		
		
		
		//构造一个本地文件
		File localFile =new File(folder,new Date().getTime()+".txt");
		
		//可通过file.getInputStream()获得上传的文件内容，然后写到其他我们想要保存的地方
		
		//保存上传的文件file的内容到本地文件localFile
		myfile.transferTo(localFile);
		
		//FileInfo是我们在com.jinsong.dto中自定义的类，保存了上传后的文件路径信息
		return new FileInfo(localFile.getAbsolutePath());
	}
	
	//下载，浏览器中输入http://localhost:8080/file/1513319584987
	@GetMapping("/{id}")
	public void download(@PathVariable String id,HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		//try(){}括号中的内容写输入输出流的话，执行完之后，JVM会帮我们自动关闭，不用在finally中手动关闭
		try (
			InputStream inputStream = new FileInputStream(new File(folder,id+".txt"));	//从本地路径中找到这个文件并存入inputStream中
			OutputStream outputStream = response.getOutputStream();
				){
			response.setContentType("application/x-download");
			response.addHeader("Content-Disposition", "attachment;filename=test.txt");//这个test.txt是我们下载时默认保存的文件名
			
			IOUtils.copy(inputStream, outputStream);	//把输入流的东西放到输出流中
			outputStream.flush();//执行输出流，就下载了
		} 
	}
}









