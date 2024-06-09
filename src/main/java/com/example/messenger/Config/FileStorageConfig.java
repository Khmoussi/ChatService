package com.example.messenger.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("storage")
@Configuration
public class FileStorageConfig {
    /**
     * Folder location for storing files
     */
    private String location = "C:\\Users\\khmou\\angular-projects\\Messenger\\src\\assets\\usersIcons";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
