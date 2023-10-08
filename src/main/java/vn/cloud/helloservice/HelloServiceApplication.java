package vn.cloud.helloservice;

import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class HelloServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloServiceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@RestController
	class HelloController {

		private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);
		private final RestTemplate restTemplate;
		private final SleepService sleepService;

		HelloController(RestTemplate restTemplate, SleepService sleepService) {
			this.restTemplate = restTemplate;
			this.sleepService = sleepService;
		}

		@GetMapping("/hello")
		public String hello() {
			LOGGER.info("---------Hello method started---------");
			LOGGER.error("---------Hello method started, id missing!---------");
			ResponseEntity<String> responseEntity = this.restTemplate.postForEntity("https://httpbin.org/post", "Hello, Cloud!", String.class);
			return responseEntity.getBody();
		}

		@GetMapping("/exception")
		public String exception() {
			throw new IllegalArgumentException("This id is invalid");
		}

		@GetMapping("/sleep")
		public Long sleep(@RequestParam Long ms) {
			Long result = this.sleepService.doSleep(ms);
			return result;
		}

		@ExceptionHandler(value = { IllegalArgumentException.class })
		protected ResponseEntity<String> handleConflict(IllegalArgumentException ex) {
			LOGGER.error(ex.getMessage(), ex);
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}

	@Service
	class SleepService {
//		@Timed(value = "do.sleep.method.timed")
//		@NewSpan(value = "do-sleep-method-span")
		@Observed(name = "do.sleep.method.timed", contextualName = "do-sleep-method-span", lowCardinalityKeyValues = {"low", "low"})
		public Long doSleep(Long ms) {
			try {
				TimeUnit.MILLISECONDS.sleep(ms);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			return ms;
		}
	}
}
