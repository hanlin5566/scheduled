package com.hanson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;


/**
 * @author Hanson create on 2018年3月11日
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@EnableScheduling
public class ScheduledBootStarp {
	public static void main(String[] args) {
		SpringApplication.run(ScheduledBootStarp.class, args);
	}
	
	@Configuration
	public class Config {

	    @Bean
	    public RestTemplate restTemplate() {
	        return new RestTemplate();
	    }
	}
}