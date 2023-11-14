package com.nruonan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Nruonan
 * @description
 */
@SpringBootApplication
@EnableSwagger2
@EnableScheduling
public class NruonanBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(NruonanBlogApplication.class,args);
    }
}
