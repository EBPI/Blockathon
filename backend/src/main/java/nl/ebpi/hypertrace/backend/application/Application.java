package nl.ebpi.hypertrace.backend.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = { "nl.ebpi.hypertrace.backend", "nl.ebpi.hypertrace.backend", "nl.ebpi.hypertrace.backend.service" })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Configuration
	public class SpringConfig {
		@Bean
		public RestTemplate restTemplate() {
			return new RestTemplate();
		}
	}
}