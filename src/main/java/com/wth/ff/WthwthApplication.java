package com.wth.ff;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.wth.ff.mapper")
@EnableScheduling
public class WthwthApplication {

	public static void main(String[] args) {
		SpringApplication.run(WthwthApplication.class, args);
	}

}
