package com.fractal.banking;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class BankingApplication {

	private String baseUri = "https://sandbox.askfractal.com";
	@Bean
	public WebClient getWebClientBuilder(){
		return WebClient.builder().baseUrl(baseUri).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}
	
	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.paths(PathSelectors.ant("/**"))
				.apis(RequestHandlerSelectors.basePackage("com.fractal.banking.controller"))
				.build()
				.apiInfo(apiDetails());
	}
	
	private ApiInfo apiDetails() {
		return new ApiInfo("Banking Api", "Demo to acccess Fractal Banking API"
				, "1.0"
				, "Free to use"
				, new springfox.documentation.service.Contact("Karar Haider", "https://www.linkedin.com/in/webconsultant/", "khsbangash@gmail.com")
				, "API License"
				, "#"
				,Collections.emptyList());
	}
	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
	}

}
