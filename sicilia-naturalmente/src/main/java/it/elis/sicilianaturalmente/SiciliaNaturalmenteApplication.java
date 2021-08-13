package it.elis.sicilianaturalmente;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.EntityManager;

@SpringBootApplication
public class SiciliaNaturalmenteApplication {
	public static void main(String[] args) {
		SpringApplication.run(SiciliaNaturalmenteApplication.class, args);

	}
}
