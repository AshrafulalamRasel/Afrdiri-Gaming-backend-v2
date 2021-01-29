package com.afridi.gamingbackend;

import com.afridi.gamingbackend.domain.model.Role;
import com.afridi.gamingbackend.domain.model.RoleName;
import com.afridi.gamingbackend.domain.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

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
