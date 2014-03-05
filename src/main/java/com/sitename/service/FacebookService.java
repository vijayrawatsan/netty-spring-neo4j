package com.sitename.service;

import java.util.Calendar;
import java.util.Date;

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
import com.sitename.util.DateUtil;
import com.sitename.util.RestTemplateUtil;
import com.sitename.util.Utils;

@Service
public class FacebookService {


    private static final Logger LOGGER       = LoggerFactory.getLogger(FacebookService.class);

    private RestTemplate        restTemplate = RestTemplateUtil.getInstance();

    @Autowired
    private UserService         userService;

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

    private static final int SIGNATURE_EXPIRY_DAYS = 15;
    private static final String ACCESS_TOKEN = "access_token";
    private static final String EXPIRES      = "expires";

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
        int expires = Integer.parseInt(Utils.getParamValue(response, EXPIRES, null));
        Date accessTokenExpiry = DateUtil.getDate(DateUtil.getCurrentDateInIST(), Calendar.SECOND, expires - 60);
        if(StringUtils.isEmpty(accessToken)) {
            return null;
        }
        String userJsonString = restTemplate.getForObject(graphUrl + accessToken, String.class);
        JSONObject userJson = (JSONObject) JSONValue.parse(userJsonString);
        if(userJson == null) {
            return null;
        }
        String userName = (String) userJson.get("username");
        User fromDB = userService.findByUserName(userName);
        if(fromDB == null) {
            fromDB = new User();
            fromDB.setFirstName((String) userJson.get("first_name"));
            fromDB.setLastName((String) userJson.get("last_name"));
            fromDB.setGender((String) userJson.get("gender"));
            fromDB.setEmail((String) userJson.get("email"));
            fromDB.setUserName(userName);
            fromDB.setFacebookId((String) userJson.get("id"));
            fromDB.setPassword(Utils.generateRandomPassword());
            fromDB.setActive(true);
        }
        // Update these 4 values in both cases
        fromDB.setFacebookAccessToken(accessToken);
        fromDB.setFacebookAccessTokenExpiry(accessTokenExpiry);
        fromDB.setSignature(Utils.getHex(Utils.nextSessionId()+userName));
        fromDB.setSignatureExipry(DateUtil.getDate(DateUtil.getCurrentDateInIST(), Calendar.DATE, SIGNATURE_EXPIRY_DAYS));
        fromDB = userService.save(fromDB);
        return fromDB;
    }
}
