package com.example.funding;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.Connection;



@SpringBootApplication
@RestController
@EnableScheduling
@EnableCaching
public class FundingApplication {

	@Resource
	private HikariDataSource dataSource;


	public static void main(String[] args) {
		SpringApplication.run(FundingApplication.class, args);
	}

}
