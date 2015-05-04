package com.springdeveloper.cloud;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@EnableAspectJAutoProxy
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

@Component
class FileSystemClient {

	private InstanceInfo instanceInfo = null;

	@Autowired
	private DiscoveryClient discoveryClient;

	@HystrixCommand(fallbackMethod = "fallbackGetPath")
	String getPath(String path) throws InstantiationException {

		try {
			instanceInfo = discoveryClient.getNextServerFromEureka("file-system-service", false);
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			throw new InstantiationException(e.getMessage());
		}
		System.out.println("GOT: " + instanceInfo);
		String results = "{ \"URL\": \"" + instanceInfo.getHomePageUrl() + "\" }";
		return results;
	}

	public String fallbackGetPath() {
		String results = "{ \"FALLBACK\": true }";
		return results;
	}

}