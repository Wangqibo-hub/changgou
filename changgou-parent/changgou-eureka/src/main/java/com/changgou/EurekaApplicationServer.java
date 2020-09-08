package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-09 19:06
 * @description
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplicationServer {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplicationServer.class, args);
    }
}
