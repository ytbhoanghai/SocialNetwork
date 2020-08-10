package com.nguyenhai.demo.Config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/css/**", "/page/css/**",
                        "/app/**", "/page/app/**",
                        "/fonts/**", "/page/fonts/**",
                        "/fullcalendar/**", "/page/fullcalendar/**",
                        "/images/**", "/page/images/**",
                        "/js/**", "/page/js/**",
                        "/audio/**", "/page/audio/**")
                .addResourceLocations("classpath:/static/css/",
                        "classpath:/static/app/",
                        "classpath:/static/fonts/",
                        "classpath:/static/fullcalendar/",
                        "classpath:/static/images/",
                        "classpath:/static/js/",
                        "classpath:/static/audio/").setCachePeriod(86400 * 30);

        registry
                .addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }

}
