package org.uftwf.account.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by xyang on 2/16/17.
 */
public class PropertiesHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesHelper.class);
    private InputStream inputStream = null;
    private Properties properties = new Properties();

    public Properties getProperties(String fileName) {
        LOGGER.info("getProperties(): return the properties of " + fileName + "\r\n");
        try {
            inputStream = this.getClass().getResourceAsStream(fileName);
            properties.load(inputStream);
        } catch (IOException e) {

        }
        return properties;
    }
}
