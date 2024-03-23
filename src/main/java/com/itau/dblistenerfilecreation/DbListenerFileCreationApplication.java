package com.itau.dblistenerfilecreation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DbListenerFileCreationApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbListenerFileCreationApplication.class, args);
	}

}
