package com.edufocus.edufocus.user.config;

import com.edufocus.edufocus.user.intercepter.JWTInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {

    private JWTInterceptor jwtInterceptor;

    public WebConfiguration(JWTInterceptor jwtInterceptor) {
        super();

        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOriginPatterns("https://i11a701.p.ssafy.io/", "http://localhost:5173", "http://localhost:4173","http://localhost:5080","http://192.168.*.*:5173")
                .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name(), HttpMethod.HEAD.name(), HttpMethod.OPTIONS.name(),
                        HttpMethod.PATCH.name())
                .allowCredentials(true)
                .allowedHeaders("*")
                .maxAge(1800); // Pre-flight Caching
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/assets/img/");
        registry.addResourceHandler("/*.html**").addResourceLocations("classpath:/static/");
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**") // 모든 경로에 대해 인터셉터 적용
                .excludePathPatterns("/v3/api-docs/**","/swagger-resources/**","/webjars/**","/swagger-ui/**","/auth/**", "/board/**", "/user/**","/lecture/**","/qna/**", "/quiz/**","/video/**","/registration/**"); // 인증 없이 접근 가능한 경로 설정
    }
}