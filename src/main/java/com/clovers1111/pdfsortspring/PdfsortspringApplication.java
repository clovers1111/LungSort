package com.clovers1111.pdfsortspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.io.File;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableAspectJAutoProxy
public class PdfsortspringApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdfsortspringApplication.class, args);
	}

}
