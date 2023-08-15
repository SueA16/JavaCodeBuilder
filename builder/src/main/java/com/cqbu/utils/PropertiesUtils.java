package com.cqbu.utils;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class PropertiesUtils {
    private static final Properties proper = new Properties();
    private static final Map<String, String> PROPER_MAP = new ConcurrentHashMap<String, String>();

    static {
        InputStream is = null;
        try {
            is = PropertiesUtils.class.getClassLoader().getResourceAsStream("application.properties");
            proper.load(is);
            Iterator<Object> iterator = proper.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                PROPER_MAP.put(key, proper.getProperty(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getString(String key) {
        return PROPER_MAP.get(key);
    }

//    public static void main(String[] args) {
//        System.out.println(getString("db.driver.name"));
//    }
}
