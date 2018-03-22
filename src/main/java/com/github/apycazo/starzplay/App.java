package com.github.apycazo.starzplay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableFeignClients
public class App
{
    public static void main (String [] args)
    {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public RestTemplate restTemplate ()
    {
        return new RestTemplate();
    }
}
