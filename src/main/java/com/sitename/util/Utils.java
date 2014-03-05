package com.sitename.util;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
    private static SecureRandom random = new SecureRandom();
    private static final String HEXES  = "0123456789ABCDEF";

    public static String getHex(byte[] raw) {
        if(raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for(final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }
    
    public static String getHex(String string) {
        return getHex(string.getBytes());
    }

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
    //TODO-Change implementation
    public static String generateRandomPassword() {
        return "password";
    }

    public static String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }
}
