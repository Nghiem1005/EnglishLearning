package com.example.english;

import com.example.english.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class EnglishApplication {

  public static void main(String[] args) {
    SpringApplication.run(EnglishApplication.class, args);
  }

}
