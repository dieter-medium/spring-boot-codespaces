package com.example.teama;

import org.springframework.boot.SpringApplication;

public class TestTeamaApplication {

	public static void main(String[] args) {
		SpringApplication.from(TeamaApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
