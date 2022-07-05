package com.huangrx.transaction;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
 public class Conf {   
   @Bean
   public Car car() {
       return new Car();
   }
 }
