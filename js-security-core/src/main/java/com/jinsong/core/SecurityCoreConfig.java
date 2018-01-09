package com.jinsong.core;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.jinsong.core.properties.SecurityProperties;

@Configuration	//声明这是一个配置项
@EnableConfigurationProperties(SecurityProperties.class)	//使SecurityProperties.class这个配置项生效
public class SecurityCoreConfig {

}
