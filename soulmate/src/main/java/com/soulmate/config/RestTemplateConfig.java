package com.soulmate.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

@Configuration
public class RestTemplateConfig {
    /**
     *  Http get, post 요청을 보낼 때 일정한 형식에 맞춰주는 template
     *  SpringBoot 에서는 다른 서버의 API endpoint 를 호출할 때 RestTemplate 를 많이 사용한다.
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8")))
                .build();
    }
}
