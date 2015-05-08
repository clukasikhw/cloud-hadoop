package com.springdeveloper.cloud;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by trisberg on 5/4/15.
 */
@Component
class FileSystemClient {

	private InstanceInfo instanceInfo = null;

	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private DiscoveryClient discoveryClient;

	@HystrixCommand(fallbackMethod = "fallbackGetPath")
	public String getPath(String path) throws InstantiationException {

		try {
			instanceInfo = discoveryClient.getNextServerFromEureka("file-system-service", false);
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			throw new InstantiationException(e.getMessage());
		}
		System.out.println("FOUND: " + instanceInfo.getAppName());
		System.out.println("FOUND: " + instanceInfo.getMetadata().get("cluster"));
		System.out.println("FOUND: " + instanceInfo.getHostName());
		String url = instanceInfo.getHomePageUrl() + "/fspath?path=" + path;
		String results = restTemplate.getForObject(url, String.class);
		System.out.println("GOT: " + results);
		return results;
	}

	public String fallbackGetPath(String path) {
		System.out.println(this.getClass().getName() + " FALLBACK!");
		String results = "{ \"FALLBACK\": \"" + path + "\" }";
		return results;
	}

}
