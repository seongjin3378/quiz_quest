package com.seong.portfolio.quiz_quest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class QuizQuestApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizQuestApplication.class, args);
	}

}
