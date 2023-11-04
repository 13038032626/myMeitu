package com.example.meitu2;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@EnableRabbit
@ServletComponentScan
@SpringBootApplication
public class MyMeituApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyMeituApplication.class, args);
    }

}
