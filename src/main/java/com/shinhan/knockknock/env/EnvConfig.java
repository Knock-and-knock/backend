package com.shinhan.knockknock.env;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
	@PropertySource("classpath:env.properties") // env.properties 파일 소스 등록
})
public class EnvConfig {

}