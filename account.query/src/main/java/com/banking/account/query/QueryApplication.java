package com.banking.account.query;

import com.banking.account.query.api.queries.*;
import com.banking.cqrs.core.infrastructure.QueryDispacher;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QueryApplication {

	@Autowired
	private QueryDispacher queryDispacher;

	@Autowired
	private QueryHandler queryHandler;


	public static void main(String[] args) {
		SpringApplication.run(QueryApplication.class, args);
	}

	@PostConstruct
	public void registerHandlers(){
		queryDispacher.registerHandler(FindAllAccountsQuery.class,queryHandler::handle);
		queryDispacher.registerHandler(FindAccountByIdQuery.class,queryHandler::handle);
		queryDispacher.registerHandler(FindAccountByHolderQuery.class,queryHandler::handle);
		queryDispacher.registerHandler(FindAccountWithBalanceQuery.class,queryHandler::handle);

	}

	@Bean
	public OpenAPI customOpenAPI(){
		return new OpenAPI()
				.info(new Info()
						.title("Swagger MicroBanco - Reactive Banking Platform 3.0 Query")
						.version("2.3.0")
						.description("The \"MicroBanco\" project aims to develop a modern banking platform, based on microservices architecture implemented in Java 8. Using MongoDB as a NoSQL database management system, we focus on reactivity and functional programming to create an efficient system and scalable.")
						.termsOfService("http://swagger.io/terms/")
						.license(new License().name("Apache 2.0").url("http://springdoc.org/"))
				);
	}

}
