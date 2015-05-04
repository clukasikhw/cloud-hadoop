package com.springdeveloper.cloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.stereotype.Controller;

@EnableEurekaClient
@SpringBootApplication
@Controller
@EnableHystrixDashboard
public class HystrixServer {

	public static void main(String[] args) {
		new SpringApplicationBuilder(HystrixServer.class).web(true).run(args);
	}

}
