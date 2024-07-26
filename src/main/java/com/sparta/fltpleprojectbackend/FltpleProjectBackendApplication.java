package com.sparta.fltpleprojectbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.sparta.fltpleprojectbackend.review.entity", "com.sparta.fltpleprojectbackend.user.entity", "com.sparta.fltpleprojectbackend.owner.entity", "com.sparta.fltpleprojectbackend.trainer.entity"})
@EnableJpaRepositories(basePackages = {"com.sparta.fltpleprojectbackend.review.repository", "com.sparta.fltpleprojectbackend.user.repository", "com.sparta.fltpleprojectbackend.owner.repository", "com.sparta.fltpleprojectbackend.trainer.repository"})
public class FltpleProjectBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(FltpleProjectBackendApplication.class, args);
  }
}