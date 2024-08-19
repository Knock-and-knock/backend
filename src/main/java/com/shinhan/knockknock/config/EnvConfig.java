package com.shinhan.knockknock.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
		@PropertySource(value = "file:/config/env.properties", ignoreResourceNotFound = true),
		@PropertySource(value = "classpath:env.properties", ignoreResourceNotFound = true)
})
public class EnvConfig {

}