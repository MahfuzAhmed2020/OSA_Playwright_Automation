package com.osa.utility;

import java.io.InputStream;
import java.util.Properties;

public final class PropertyReader {
    private static final Properties PROPERTIES = new Properties();
    static {
        try (InputStream input = PropertyReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null)
                throw new IllegalStateException("Missing config.properties");
            PROPERTIES.load(input);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private PropertyReader() {
    }

    public static String getProperty(String key) {
        String override = System.getProperty(key);
        if (override != null)
            return override;
        String value = PROPERTIES.getProperty(key);
        if (value == null)
            throw new IllegalArgumentException("Missing property: " + key);
        return value;
    }

    public static int getInt(String key) {
        return Integer.parseInt(getProperty(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }
}
