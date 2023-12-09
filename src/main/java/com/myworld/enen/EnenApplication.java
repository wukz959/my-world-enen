package com.myworld.enen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
public class EnenApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnenApplication.class, args);
    }

}
