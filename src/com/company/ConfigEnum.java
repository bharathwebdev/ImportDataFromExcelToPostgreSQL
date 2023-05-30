package com.company;

enum Config {
    INSTANCE;

    public String getConfig(String configName, String defaultValue) {
        return System.getenv(configName) == null ? defaultValue : System.getenv(configName);
    }

    public String getConfig(String configName) {
        return System.getenv(configName);
    }

}
