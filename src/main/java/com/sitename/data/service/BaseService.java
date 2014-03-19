package com.sitename.data.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.sitename.data.config.DataConfig;


public abstract class BaseService {

    protected static final ApplicationContext dataContext = new AnnotationConfigApplicationContext(DataConfig.class);
}
