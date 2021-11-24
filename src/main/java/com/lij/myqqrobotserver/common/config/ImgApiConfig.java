package com.lij.myqqrobotserver.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Celphis
 */
@Configuration
@ConfigurationProperties(prefix = "cawler")
public class ImgApiConfig {
    private Map<String, String> pictureMap = new HashMap<>();

    public Map<String, String> getPictureMap() {
        return pictureMap;
    }

    public void setPictureMap(Map<String, String> pictureMap) {
        this.pictureMap = pictureMap;
    }
}
