package com.sitename.util;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    /**
     * Find key value in queryString. Returns default value if not found
     * 
     * @param queryString
     * @param key
     * @param defaultVal
     * @return
     */
    public static String getParamValue(String queryString, String key, String defaultVal) {
        String result = defaultVal;
        try {
            List<NameValuePair> pairs = URLEncodedUtils.parse(queryString, Charset.forName("UTF-8"));
            for(NameValuePair nvp : pairs) {
                if(nvp.getName().equals(key)) {
                    result = nvp.getValue();
                }
            }
        }
        catch (Exception e) {
            LOGGER.error("Exception in getParamValue : " + e.getMessage(), e);
        }
        return result;
    }
    
    public static String generateRandomPassword() {
        return "password";
    }
}
