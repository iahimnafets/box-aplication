package com.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan( basePackages = { "com.calculator" } )
@SpringBootApplication
public class MyApplication {


	public static void main(String[] args) {
		SpringApplication.run(MyApplication.class, args);
	}

}
