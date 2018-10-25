package com.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoginApplication{

	//private static final Logger logger = LogManager.getLogger(LoginApplication.class);

	public static void main(String[] args) {
		
		//logger.info("Application started");
		SpringApplication.run(LoginApplication.class, args);
	}    

}
