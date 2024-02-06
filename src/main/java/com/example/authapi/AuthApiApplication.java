package com.example.authapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/*
 * Пусть весь мир встанет против меня, я продолжу идти своим путем.
 */

@SpringBootApplication
@ComponentScan("com.example")
public class AuthApiApplication {
  public static void main(String[] args) {
    SpringApplication.run(AuthApiApplication.class, args);
  }
}
