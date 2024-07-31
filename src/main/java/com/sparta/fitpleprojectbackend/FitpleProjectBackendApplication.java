package com.sparta.fitpleprojectbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FitpleProjectBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(FitpleProjectBackendApplication.class, args);
  }

}
