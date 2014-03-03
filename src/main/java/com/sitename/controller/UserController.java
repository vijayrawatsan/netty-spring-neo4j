package com.sitename.controller;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sitename.domain.RestResponse;
import com.sitename.domain.User;
import com.sitename.service.UserService;

@SuppressWarnings("serial")
@Controller
@RequestMapping("/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService         userService;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponse> addUser(@RequestBody User user) {
        ResponseEntity<RestResponse> response = null;
        try {
            response = new ResponseEntity<RestResponse>(new RestResponse(Boolean.TRUE, null, userService.save(user)), HttpStatus.OK);
        }
        catch (final Exception e) {
            LOGGER.error("Exception in addUser : Exception is : " + e.getMessage(), e);
            response = new ResponseEntity<RestResponse>(new RestResponse(Boolean.FALSE, new ArrayList<String>() {{ add(e.getMessage()); }}, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponse> findUser(@PathVariable Long id) {
        ResponseEntity<RestResponse> response = null;
        try {
            User user = userService.findById(id);
            if(user != null)
                response = new ResponseEntity<RestResponse>(new RestResponse(Boolean.TRUE, null, user), HttpStatus.OK);
            else 
                response = new ResponseEntity<RestResponse>(new RestResponse(Boolean.TRUE, null, user), HttpStatus.NOT_FOUND);
            
        }
        catch (final Exception e) {
            LOGGER.error("Exception in findUser : Exception is : " + e.getMessage(), e);
            response = new ResponseEntity<RestResponse>(new RestResponse(Boolean.FALSE, new ArrayList<String>() {{ add(e.getMessage()); }}, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponse> listUsers() {
        ResponseEntity<RestResponse> response = null;
        try {
            response = new ResponseEntity<RestResponse>(new RestResponse(Boolean.TRUE, null, userService.findAll()), HttpStatus.OK);
        }
        catch (final Exception e) {
            LOGGER.error("Exception in listUsers : Exception is : " + e.getMessage(), e);
            response = new ResponseEntity<RestResponse>(new RestResponse(Boolean.FALSE, new ArrayList<String>() {{ add(e.getMessage()); }}, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
