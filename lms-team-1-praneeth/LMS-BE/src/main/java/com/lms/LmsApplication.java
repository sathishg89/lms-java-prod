package com.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LmsApplication {

	//private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LmsApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(LmsApplication.class, args);

	}

}
