package com.example.demo.config;

import com.siot.IamportRestClient.IamportClient;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * payment setting
 */
@Configuration
@PropertySource(value = "classpath:application.yml", factory = YamlLoadFactory.class)
@Getter
@ToString
public class AppConfig {
    @Value("${iamport.imp_key}")
    String apiKey;
    @Value("${iamport.imp_secret}")
    String secretKey = "REST API Secret를 입력합니다.";

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, secretKey);
    }

}

