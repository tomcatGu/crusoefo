package com.crusoe.fo.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);

	}

	// @Bean
	/*
	 * public CommandLineRunner init(final RepositoryService repositoryService,
	 * final RuntimeService runtimeService, final TaskService taskService) {
	 * 
	 * return new CommandLineRunner() {
	 * 
	 * @Override public void run(String... strings) throws Exception {
	 * RepositoryService // System.out.println( //
	 * "Number of process definitions : " +
	 * repositoryService.createProcessDefinitionQuery().count()); //
	 * System.out.println("Number of tasks : " +
	 * taskService.createTaskQuery().count()); //
	 * runtimeService.startProcessInstanceByKey("Expense"); //
	 * System.out.println("Number of tasks after process start: " +
	 * taskService.createTaskQuery().count()); } }; }
	 */
}
