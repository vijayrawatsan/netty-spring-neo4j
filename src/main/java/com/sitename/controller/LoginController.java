package com.sitename.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sitename.domain.User;
import com.sitename.service.FacebookService;
import com.sitename.util.RestResponse;
import com.sitename.util.UserDto;

@Controller
@RequestMapping("/login")
class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    
    @Autowired
    private FacebookService facebookService;
    
    @RequestMapping(value = "/facebook", method = RequestMethod.GET)
    public String faceBookRedirect() {
        return "redirect:" + facebookService.getOAuthUrl();
    }

    @SuppressWarnings("serial")
    @RequestMapping(value = "/facebook/callback", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<RestResponse> facebookCallback(@RequestParam(required = false) String code, @RequestParam(required = false, value = "error_reason") final String errorReason,
            @RequestParam(required = false, value = "error") final String error, @RequestParam(required = false, value = "error_description") final String errorDescription) {
        ResponseEntity<RestResponse> response = null;
        try {
            if(!StringUtils.isEmpty(code)) {
                User user = facebookService.getUser(code);
                if(user != null) {
                    response = new ResponseEntity<RestResponse>(new RestResponse(Boolean.TRUE, null, new UserDto(user)), HttpStatus.OK);
                } else {
                    response = new ResponseEntity<RestResponse>(new RestResponse(Boolean.FALSE, new ArrayList<String>() {{ add(errorReason); add(error); add(errorDescription); }}, null), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        catch (final Exception e) {
            LOGGER.error("Exception occurred."+e.getMessage(), e);
            response = new ResponseEntity<RestResponse>(new RestResponse(Boolean.FALSE, new ArrayList<String>() {{ add(e.getMessage()); }}, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

}
