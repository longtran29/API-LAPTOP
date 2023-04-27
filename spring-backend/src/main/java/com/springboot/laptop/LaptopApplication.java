package com.springboot.laptop;

import com.springboot.laptop.controller.ProductController;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(title = "Shop Laptop API", version = "1.0.0"),
		servers = {@Server(url = "http://localhost:8080") },
		tags = {@Tag(name = "Laptop", description = "Shop sells laptop.")}
)
//@SecurityScheme(name = "bamboo_store", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class LaptopApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(LaptopApplication.class, args);
		System.out.println("Application context get all products " + context.getBean(ProductController.class).getActiveProducts());
	}

}
