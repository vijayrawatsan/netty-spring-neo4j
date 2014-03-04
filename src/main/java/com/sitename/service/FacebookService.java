package com.sitename.service;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.sitename.domain.User;
import com.sitename.util.RestTemplateUtil;
import com.sitename.util.Utils;

@Service
public class FacebookService {

    private static final Logger LOGGER       = LoggerFactory.getLogger(FacebookService.class);

    private RestTemplate        restTemplate = RestTemplateUtil.getInstance();
    
    @Autowired
    private UserService userService;
    
    @Value(value = "${fb.oauth.appId}")
    private String              appId;

    @Value(value = "${fb.oauth.appSecret}")
    private String              appSecret;

    @Value(value = "${fb.oauth.permissions}")
    private String              permissions;

    @Value(value = "${fb.oauth.login.url}")
    private String              loginUrl;

    @Value(value = "${fb.oauth.callback.url}")
    private String              callbackUrl;

    @Value(value = "${fb.oauth.token.url}")
    private String              tokenUrl;

    @Value(value = "${fb.graphurl.me}")
    private String              graphUrl;

    private static final String ACCESS_TOKEN = "access_token";

    public String getOAuthUrl() {
        return loginUrl + "&client_id=" + appId + "&scope=" + permissions + "&redirect_uri=" + callbackUrl;
    }

    public User getUser(String code) {
        String url = tokenUrl + "&client_id=" + appId + "&redirect_uri=" + callbackUrl + "&client_secret=" + appSecret + "&code=" + code;
        String response = restTemplate.getForObject(url, String.class);
        if(response.contains("error")) {
            LOGGER.error("Error Url : {0} and Response : {1}", url, response);
            return null;
        }
        String accessToken = Utils.getParamValue(response, ACCESS_TOKEN, null);
        if(StringUtils.isEmpty(accessToken)) {
            return null;
        }
        String userJsonString = restTemplate.getForObject(graphUrl + accessToken, String.class);
        JSONObject userJson = (JSONObject) JSONValue.parse(userJsonString);
        if(userJson == null) {
            return null;
        }
        User user = new User();
        user.setFirstName((String) userJson.get("first_name"));
        user.setLastName((String) userJson.get("last_name"));
        user.setGender((String) userJson.get("gender"));
        user.setEmail((String) userJson.get("email"));
        user.setUserName((String) userJson.get("username"));
        user.setFacebookId((String) userJson.get("id"));
        User fromDB = userService.findByUserName(user.getUserName());
        if(fromDB == null) {
            user.setPassword(Utils.generateRandomPassword());
            user.setActive(true);
            fromDB = userService.save(user);
        }
        return fromDB;
    }
}
