package com.coldev.estore.config;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CorsConfiguration {

    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                registry.addMapping("/**")
                        .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
                                HttpMethod.OPTIONS.name(), HttpMethod.DELETE.name())
                        .allowedHeaders("*")
                        .allowedOrigins("http://localhost:80",
                                "http://localhost:3000",
                                "http://localhost:8082",
                                "http://localhost:8081"
                                // In case there is a static web resource link, put it here,
                                // for example "https://btp.realivecreatives.com"
                        );
            }
        };
    }

    ;
}
