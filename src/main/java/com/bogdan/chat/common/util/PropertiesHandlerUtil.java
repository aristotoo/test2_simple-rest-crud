package com.bogdan.chat.common.util;

import java.util.ResourceBundle;

public class PropertiesHandlerUtil {
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("query");

    private PropertiesHandlerUtil() {
    }

    public static String getValue(String key) {
        return BUNDLE.getString(key);
    }

}
