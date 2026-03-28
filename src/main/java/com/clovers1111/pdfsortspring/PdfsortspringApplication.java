package com.clovers1111.pdfsortspring;

import org.apache.pdfbox.io.RandomAccessStreamCache;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.io.File;

@SpringBootApplication
@EnableAspectJAutoProxy
public class PdfsortspringApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdfsortspringApplication.class, args);
	}

}
