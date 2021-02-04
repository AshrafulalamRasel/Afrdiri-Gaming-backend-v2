package com.afridi.gamingbackend;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@AllArgsConstructor
public class GamingBackendApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(GamingBackendApplication.class, args);
	}


	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(GamingBackendApplication.class);
	}

}
