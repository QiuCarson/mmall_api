package com.carson.mmall.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "custom")
public class CustomConfig {
    /**
     * 图片的地址
     */
    private String imageHost;

    /**
     * 加密
     */
    private String UserPasswordSalt;

    /**
     * 图片保存路径
     */
    private String imageSavePath;
}
