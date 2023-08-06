package com.alfonsoristorato.igdbscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class IGDBScraperApplication {
    public static void main(String[] args) {
        SpringApplication.run(IGDBScraperApplication.class, args);
    }

}
