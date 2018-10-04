package com.cloud.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.constants.CommonConstants;
import com.cloud.model.User;
import com.cloud.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Added the function to get time for authenticated users
     * @return String 
     */
    @RequestMapping(value={"/time"}, method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String time(){
    	
        return new Date().toString();
    }

    /**
     * Added the to register the users and also check is the user is already present
     * @return String 
     */
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ResponseBody
    public String createNewUser(@RequestBody User user, BindingResult bindingResult) {
        
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
           return CommonConstants.USER_ALREADY_EXISTS;
        }
       
        userService.saveUser(user);
        return CommonConstants.USER_REGISTERATION_SUCCESS;
    }
    
    /**
     * Added the function to get transaction for authenticated users
     * @return String 
     */
    

}
