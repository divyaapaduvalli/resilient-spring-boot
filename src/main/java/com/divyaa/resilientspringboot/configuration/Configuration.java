package com.divyaa.resilientspringboot.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author Divyaa P
 */
@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().rootUri("http://localhost:9090")
                .build();
    }
}
