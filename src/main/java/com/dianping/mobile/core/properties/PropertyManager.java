package com.dianping.mobile.core.properties;

import com.dianping.mobile.core.exception.ApplicationRuntimeException;
import com.dianping.mobile.core.util.ApiUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyManager {

    private static Map<String, Properties> propertiesCache = new HashMap<String, Properties>();
    private static final Logger log = Logger.getLogger(PropertyManager.class);

    private PropertyManager() {
    }

    public static Properties getProperties(String category) throws ApplicationRuntimeException {
        String fileName = "/properties/" + category + ".properties";
        if (propertiesCache.containsKey(fileName)) {
            return propertiesCache.get(fileName);
        }
        Class clazz = null;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 1; i < stackTrace.length; ++i) {
            String className = stackTrace[i].getClassName();
            if (!className.equals(PropertyManager.class.getName())) {
                try {
                    clazz = Class.forName(className);
                    break;
                } catch (ClassNotFoundException e) {
                }
            }
        }
        InputStream in = null;
        try {
            in = clazz.getResourceAsStream(fileName);
            if (in == null) {
                return null;
            }
            Properties properties = new Properties();
            properties.load(in);
            propertiesCache.put(fileName, properties);
            return properties;
        } catch (IOException e) {
            log.warn("fail to load property, category:" + category, e);
            throw new ApplicationRuntimeException("fail to load property, category:" + category, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.warn("close InputStream error", e);
                }
            }

        }
    }

    public static String get(String category, String name) {
        Properties properties = getProperties(category);
        if (properties == null) {
            throw new ApplicationRuntimeException("property file:" + category + " does not find");
        }
        String value = properties.getProperty(name);
        return ApiUtil.unicodeToChinese(value);
    }

}
