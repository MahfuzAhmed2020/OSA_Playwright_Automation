package com.osa.utility;

import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.JSONObject;
import org.testng.Assert;

public class Utilities {
    
    public static JSONObject getJsonObject(Map<String, String> values) {
        return new JSONObject(values);
    }

    public static void verifyStatu(String expected, String actual) {
        verifyStatus(expected, actual);
    }

    public static void verifyStatus(String expected, String actual) {
        Assert.assertEquals(actual, expected);
    }

    public static Logger getLog(Class<?> type) {
        return LogManager.getLogger(type);
    }

    
}
