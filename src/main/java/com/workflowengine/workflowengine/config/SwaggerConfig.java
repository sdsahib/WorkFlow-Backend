package com.workflowengine.workflowengine.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

import static com.workflowengine.workflowengine.utils.Constants.BEARER;
import static com.workflowengine.workflowengine.utils.Constants.KEY_HEADER_AUTHORIZATION;

/**
 * Created by ajaiswal on 2/22/2019.
 */
@Configuration
@EnableSwagger2
@ComponentScan(basePackages = "com.workflowengine.workflowengine.controller")
public class SwaggerConfig {

    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Autowired
    private BuildProperties buildProperties;

    ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("REST API for WorkFlow module.")
                .description("Documentation for Greenhawk WorkFlow API.")
                .termsOfServiceUrl("").version(buildProperties.getVersion())
                .contact(new Contact("TEKsystems Global Services", "https://www.teksystems.com/en/services", "ajaiswal@teksystems.com"))
                .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.workflowengine.workflowengine.controller")).build()
                .apiInfo(apiInfo())
                .host("localhost:8080")
//                .pathMapping(contextPath)
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex("/.*")).build();
    }

    private List<SecurityReference> defaultAuth() {
        final AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
        return Collections.singletonList(new SecurityReference(BEARER, authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey(BEARER, KEY_HEADER_AUTHORIZATION, "header");
    }

}
