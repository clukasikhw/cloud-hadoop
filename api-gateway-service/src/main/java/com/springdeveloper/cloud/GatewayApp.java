package com.springdeveloper.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@RestController
public class GatewayApp {

	@Autowired
	private FileSystemClient fsClient;

	public static void main(String[] args) {
		SpringApplication.run(GatewayApp.class, args);
	}

	@RequestMapping("/fspath")
	public String fspath(@RequestParam(value = "path", defaultValue = "/") String path) {
		String results = null;
		try {
			results = fsClient.getPath(path);
		} catch (InstantiationException e) {
			return "{ \"OUCH\": true }";
		}
		return results;
	}
}

