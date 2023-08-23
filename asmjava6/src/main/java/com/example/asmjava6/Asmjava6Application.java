package com.example.asmjava6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.controller","com.example.entity","com.example.jparepository"
		,"com.example.interceptor","com.example.rest","com.example.service","com.example.service.impl","com.example.config"})
@EnableJpaRepositories("com.example.jparepository")
@EntityScan("com.example.entity")
public class Asmjava6Application {

	public static void main(String[] args) {
		SpringApplication.run(Asmjava6Application.class, args);
		
	}

}
		