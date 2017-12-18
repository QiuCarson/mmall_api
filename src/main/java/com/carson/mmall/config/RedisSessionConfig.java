package com.carson.mmall.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@EnableRedisHttpSession
public class RedisSessionConfig {

}
