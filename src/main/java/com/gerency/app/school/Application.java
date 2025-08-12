package com.alangodoy.studioApp.s;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@SpringBootApplication
@EnableScheduling // Habilita o agendamento de tarefas
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
