package com.jinsong.wiremock;


import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;


import com.github.tomakehurst.wiremock.client.WireMock;



public class MockServer {

	public static void main(String[] args) throws IOException {
		//设置端口
		WireMock.configureFor(8080);	//本地直接设端口号，如果非本地，用另外带3个参数的configureFor
		
		
		//移除之前的配置以便加载新的配置，比如HTTP响应头改变了
		WireMock.removeAllMappings();
		
		//测试用例
		mock("/order/1","01");	//URL“/order/1”的返回JSON串放在src/main/resources/mock/response/01.txt里面
		mock("/order/2", "02");
	}
	
	private static void mock(String url,String file) throws IOException{
		ClassPathResource resource = new ClassPathResource("mock/response/"+file+".txt");
		String content = StringUtils.join(FileUtils.readLines(resource.getFile(),"UTF-8").toArray(),"\n");
		WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo(url)).willReturn(WireMock.aResponse().withBody(content).withStatus(200)));
	}
}
