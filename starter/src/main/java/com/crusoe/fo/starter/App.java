package com.crusoe.fo.starter;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration

@ComponentScan

@EnableAutoConfiguration
@SpringBootApplication(proxyBeanMethods = false)
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	//@Bean
	public CommandLineRunner init(final RepositoryService repositoryService, final RuntimeService runtimeService,
			final TaskService taskService) {

		return new CommandLineRunner() {
			@Override
			public void run(String... strings) throws Exception {
			//	System.out.println(
			//			"Number of process definitions : " + repositoryService.createProcessDefinitionQuery().count());
			//	System.out.println("Number of tasks : " + taskService.createTaskQuery().count());
			//	runtimeService.startProcessInstanceByKey("Expense");
			//	System.out.println("Number of tasks after process start: " + taskService.createTaskQuery().count());
			}
		};
	}

}
