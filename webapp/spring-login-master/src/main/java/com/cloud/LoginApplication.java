package com.cloud;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class LoginApplication{

	//private static final Logger logger = LogManager.getLogger(LoginApplication.class);

	public static void main(String[] args) {
		//logger.info("Application started");

		SpringApplication.run(LoginApplication.class, args);
	}
	

	@RequestMapping(value={"/"}, method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String time(){
    	
    	return new Date().toString();
    }

    

}
