package com.statemnt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages ={"com"})
public class App extends SpringBootServletInitializer implements CommandLineRunner {

	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(App.class);
	}

	
	public static void main(String[] args) { 
		
		System.out.println("========Spring Boot Started===============");
		SpringApplication.run(App.class, args);
	
	}
	
	public void run(String... args) throws Exception {
	}
}