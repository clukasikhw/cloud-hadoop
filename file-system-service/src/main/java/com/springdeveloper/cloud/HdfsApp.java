package com.springdeveloper.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@RestController
public class HdfsApp implements EnvironmentAware {

	private String profile;

	@Autowired
	private HdfsClient hdfsClient;

	public static void main(String[] args) {
		SpringApplication.run(HdfsApp.class, args);
	}

    @RequestMapping("/fspath")
	public Map<String, Object> fspath(@RequestParam(value="path", defaultValue="/") String path) throws InstantiationException {
		return hdfsClient.getHdfsPath(path);
	}

	public void setEnvironment(Environment environment) {
		this.profile = Arrays.asList(environment.getActiveProfiles()).toString();
	}
}
