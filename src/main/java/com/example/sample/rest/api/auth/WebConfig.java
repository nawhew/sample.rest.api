package com.example.sample.rest.api.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginUserArgumentResolver loginUserArgumentResolver;

    /**
     * LoginUserArgumentResolver를 스프링에서 인식 될 수 있도록
     * 스프링 WebMvcConfigurer의 ArgumentResolvers에 해당 resolver를 추가한다.
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(this.loginUserArgumentResolver);
    }
}
