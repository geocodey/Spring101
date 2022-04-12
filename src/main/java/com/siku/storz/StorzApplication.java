package com.siku.storz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StorzApplication {

    public static void main(String[] args) {
        final SpringApplication application = new SpringApplication(StorzApplication.class);
        application.setAdditionalProfiles(Profiles.DEV);
        application.run(args);
    }

}
