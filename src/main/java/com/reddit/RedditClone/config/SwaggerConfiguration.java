package com.reddit.RedditClone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//Swagger http://localhost:8080/swagger-ui.html#/

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket redditCloneApi(){
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any()).build().apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo(){
        return new ApiInfoBuilder().title("Reddit clone Apis").version("1.2").description("Api for reddit clone application").contact(new Contact("Ankur Biswas","https://github.com/crimson666?tab=overview&from=2024-03-01&to=2024-03-10","ankurbsws@gmail.com")).license("Apache License Version 2.0").build();
    }
}
