package com.springdeveloper.cloud;

import org.apache.hadoop.fs.FileStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.data.hadoop.fs.FsShell;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class HdfsApp implements EnvironmentAware {

	@Autowired
	private FsShell fsShell;

	private String profile;

	public static void main(String[] args) {
		SpringApplication.run(HdfsApp.class, args);
	}

//    @RequestMapping("/env")
//	public Map<String, Object> env() {
//		List<String> envVars = new ArrayList<String>();
//		Map<String, String> env = System.getenv();
//		for (Map.Entry<String, String> entry : env.entrySet()) {
//			envVars.add(entry.getKey() + " = " + entry.getValue());
//		}
//		List<String> sysProps = new ArrayList<String>();
//		Properties props = System.getProperties();
//		Enumeration<?> propNames = props.propertyNames();
//		while (propNames.hasMoreElements()) {
//			String prop = propNames.nextElement().toString();
//			sysProps.add(prop + " = " + props.getProperty(prop));
//		}
//		Map<String, Object> results = new HashMap<String, Object>();
//		results.put("profile", profile);
//		results.put("envvars", envVars);
//		results.put("sysprops", sysProps);
//		return results;
//	}
//
    @RequestMapping("/fspath")
	public Map<String, Object> fspath(@RequestParam(value="path", defaultValue="/") String path) {
		List dirs = new ArrayList();
		List files = new ArrayList();
		for (FileStatus fs : fsShell.ls(path)) {
			if (!path.equals(fs.getPath().toUri().getRawPath())) {
				if (fs.isDirectory()) {
					dirs.add(fs.getPath().getName());
				} else {
					files.add(fs.getPath().getName());
				}
			}
		}
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("directories", dirs);
		results.put("files", files);
		return results;
	}

	public void setEnvironment(Environment environment) {
		this.profile = Arrays.asList(environment.getActiveProfiles()).toString();
	}
}
