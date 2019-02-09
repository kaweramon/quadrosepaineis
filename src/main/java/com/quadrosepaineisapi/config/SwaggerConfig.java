package com.quadrosepaineisapi.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()
          .apis(RequestHandlerSelectors.basePackage("com.quadrosepaineisapi.resource"))              
          .paths(PathSelectors.any())
          .build()
          .apiInfo(apiInfo());                                           
    }
	
	private ApiInfo apiInfo() {
	    return new ApiInfo(
	      "REST API da Quadros e Painéis", 
	      "A quadros e painéis é uma empresa familiar que produz quadros em geral usando elementos naturais encontrados na nosso região da Paraíba", 
	      "V 0.0.1", 
	      "Terms of service", 
	      new Contact("Kawe Ramon", "https://kaweramon.github.io", "kawe.ufpbsi@gmail.com"), 
	      "License of API", "API license URL", Collections.emptyList());
	}
	
}
