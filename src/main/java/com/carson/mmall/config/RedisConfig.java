package com.carson.mmall.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching // 启用缓存特性
public class RedisConfig extends CachingConfigurerSupport {

}
