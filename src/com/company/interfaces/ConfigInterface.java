package com.company.interfaces;

public interface ConfigInterface {
//    default public String getConfig(String configName, String defaultValue) {
//        return System.getenv(configName) == null ? defaultValue : System.getenv(configName);
//   }
//   default public String getConfig(String configName) {
//        return System.getenv(configName);
//   }

    public String getConfig(String configName, String defaultValue);
    public String getConfig(String configName);
}
