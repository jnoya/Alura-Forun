
package com.br.alura.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@SpringBootApplication
public class ForumApplication {

	public static void main(String[] args) {

		SpringApplication.run(ForumApplication.class, args);

	}

	@Bean
	public OpenAPI customOpenAPI() {

		return new OpenAPI().info(new Info().title("Challenge Forum Alura").version("1.0")
				.description("Uma API REST para o funcionamento de um fórum, onde poderá participar "
						+ "obtendo e entregando conhecimento sobre diferentes tópicos referentes a diversos cursos da Alura")
				.termsOfService("http://swagger.io/terms/")
				.license(new License().name("Apache 2.0").url("http://springdoc.org")));

	}

}
