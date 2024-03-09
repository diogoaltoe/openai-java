package me.diogo.openaijava;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "OpenAI Java",
        description = "OpenAI GPT integration project using Java",
        version = "1.0.0",
        contact = @Contact(
                name = "Diogo Altoé Lopes",
                url = "https://diogo.me",
                email = "info@diogo.me"
        ),
        license = @License(
                name = "MIT License",
                url = "https://opensource.org/licenses/MIT"
        )
))
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}

