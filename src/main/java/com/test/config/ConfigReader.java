package com.test.config;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;
    
    static{
        try(FileInputStream file = new FileInputStream("src/main/java/com/test/resources/config.properties")){
            properties = new Properties();
            properties.load(file);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    public static String getProperty(String key){
        return properties.getProperty(key).trim();
    }
    public static String getBaseUrl(){
        return getProperty("baseURL");
    }
    public static String getAuthUsername(){
        return getProperty("authUsername");
    }
    public static String getAuthPassword(){
        return getProperty("authPassword");
    }
    public static String getReportPath(){
        return getProperty("reportPath");
    }
}
