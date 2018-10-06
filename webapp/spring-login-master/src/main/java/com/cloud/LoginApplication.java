package com.cloud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoginApplication {

	private static final Logger logger = LogManager.getLogger(LoginApplication.class);
	
    public static void main(String[] args) {
    	logger.info("Application started");
        SpringApplication.run(LoginApplication.class, args);
    }
}
