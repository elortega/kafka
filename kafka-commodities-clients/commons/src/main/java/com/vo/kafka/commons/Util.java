package com.vo.kafka.commons;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class with common functionality and constants.
 */
public class Util {
    /**
     * JVM system property key used to point to the properties file to load.
     */
    private static final String PRODUCER_CONFIG_SYS_PROP = "configFile";

    /**
     * Loads the properties defined in file pointed by the JVM {@value #PRODUCER_CONFIG_SYS_PROP}
     * property.
     */
    public static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(
                System.getProperty(PRODUCER_CONFIG_SYS_PROP))) {
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return props;
    }

    /**
     * @return the path to the temporary directory configured in the properties.
     */
    public static String getSystemTempDirectory(){
        return loadProperties().get("tmp.directory").toString();
    }

}


