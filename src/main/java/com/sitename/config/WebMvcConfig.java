package com.sitename.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@Configuration
@Import(value = { DataConfig.class })
@ComponentScan(basePackages = "com.sitename")
class WebMvcConfig extends WebMvcConfigurerAdapter {
}