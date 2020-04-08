package com.bncrypted.authenticator.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    public static final String SESSION_TAG = "Session";
    public static final String USER_TAG = "User";

    @Bean
    public Docket getApiDocs() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(getApiInfo())
                .tags(
                        new Tag(SESSION_TAG, "Session management"),
                        new Tag(USER_TAG, "User management")
                );
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Authenticator API")
                .description("Provides JWT-based user and session management")
                .license("")
                .licenseUrl("")
                .version("1.0.0")
                .termsOfServiceUrl("")
                .build();
    }

}
