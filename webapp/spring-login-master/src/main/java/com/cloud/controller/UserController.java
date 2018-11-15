package com.cloud.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.constants.CommonConstants;
import com.cloud.model.Status;
import com.cloud.model.User;
import com.cloud.service.UserService;
import com.timgroup.statsd.StatsDClient;

@RestController
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;
    
    @Autowired
    private StatsDClient statsDClient;

    /**
     * Added the function to get time for authenticated users
     * @return String
     */
    @RequestMapping(value={"/time"}, method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String time(){
    	    	
    	statsDClient.incrementCounter("endpoint.time.http.get");
    	logger.info("Get Time");
    	return new Date().toString();
    }

    /**
     * Added the to register the users and also check is the user is already present
     * @return String
     */
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ResponseBody
    public String createNewUser(@RequestBody User user, BindingResult bindingResult) {

   	    statsDClient.incrementCounter("endpoint.user.register.http.post");
        logger.info("Create New User - Start");

        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            return CommonConstants.USER_ALREADY_EXISTS;
        }

        userService.saveUser(user);

        logger.info("Create New User - End");

        return CommonConstants.USER_REGISTERATION_SUCCESS;
    }

    /**
     * Logout the user from Spring context
     * @param request
     * @param response
     * @return
     */
  @RequestMapping(value="/logout", method = RequestMethod.GET)
	public void logout (HttpServletRequest request, HttpServletResponse response) {
    	
	  statsDClient.incrementCounter("endpoint.logout.http.get");
    logger.info("Logout - Start");
    	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){    
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		
		logger.info("Logout - End");
	}
    
    

    @RequestMapping(value="/reset", method = RequestMethod.GET)
	public Status generateResetToken(@RequestParam("email") String email) {

    statsDClient.incrementCounter("endpoint.reset.http.get");
    Status status = new Status();
		logger.info("generateResetToken - Start ");
		
		try 
		{
			User user = userService.findUserByEmail(email);
			if(user != null)
			{
				userService.sendMessage(email);
			}
			status.setStatusCode(CommonConstants.SUCCESS);
			status.setMessage(CommonConstants.PASSWORD_RESET_EMAIL);
			
		}
		catch (Exception e) 
		{	
			logger.error("Exception in generating reset token : " + e.getMessage());
			status.setStatusCode(CommonConstants.SEND_RESET_EMAIL_FAILURE);
			status.setMessage(e.getMessage());
		}

		logger.info("generateResetToken - End ");

		return status;

	}

}

