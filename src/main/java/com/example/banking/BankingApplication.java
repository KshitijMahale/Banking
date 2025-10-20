package com.example.banking;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Kshitij's Banking Application",
				description = "Backend REST API",
				version = "v1.0",
				contact = @Contact(
						name = "Kshitij Mahale",
						email = "kshitijmahale02@gmail.com",
						url = "https://github.com/KshitijMahale/Banking"
				),
				license = @License(
						name = "Kshitij",
						url = "https://github.com/KshitijMahale/Banking"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Kshitij's Banking Application",
				url = "https://github.com/KshitijMahale/Banking"
		)
)
public class BankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
	}

}
