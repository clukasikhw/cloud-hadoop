package com.springdeveloper.cloud;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.hadoop.fs.FileStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.fs.FsShell;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by trisberg on 5/7/15.
 */
@Component
class HdfsClient {

	@Autowired
	FsShell fsShell;

	@HystrixCommand(fallbackMethod = "fallbackGetHdfsPath")
	public Map<String, Object> getHdfsPath(String path) throws InstantiationException {
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

	public Map<String, Object> fallbackGetHdfsPath(String path) {
		System.out.println(this.getClass().getName() + " FALLBACK!");
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("fallback", "{ \"FALLBACK\": \"" + path + "\" }");
		return results;
	}

}
