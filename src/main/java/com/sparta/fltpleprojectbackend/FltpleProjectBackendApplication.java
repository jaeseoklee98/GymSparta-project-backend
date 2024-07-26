package com.sparta.fltpleprojectbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing

@SpringBootApplication
public class FltpleProjectBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(FltpleProjectBackendApplication.class, args);
  }
}