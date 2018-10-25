package com.cloud.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.constants.CommonConstants;
import com.cloud.model.User;
import com.cloud.service.AmazonClient;
import com.cloud.service.UserService;

@RestController
public class UserController {

    //private static final //logger = LogManager.get//logger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * Added the function to get time for authenticated users
     * @return String
     */
    @RequestMapping(value={"/time"}, method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String time(){

        //logger.info("Get Time");
        return new Date().toString();
    }

    /**
     * Added the to register the users and also check is the user is already present
     * @return String
     */
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ResponseBody
    public String createNewUser(@RequestBody User user, BindingResult bindingResult) {

        //logger.info("Create New User - Start");

        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            return CommonConstants.USER_ALREADY_EXISTS;
        }

        userService.saveUser(user);

        //logger.info("Create New User - End");

        return CommonConstants.USER_REGISTERATION_SUCCESS;
    }

    /**
     * Logout the user from Spring context
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public void logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    }


}