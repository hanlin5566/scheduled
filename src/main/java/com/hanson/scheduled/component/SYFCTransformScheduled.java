package com.hanson.scheduled.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author Hanson create on 2019年3月7日
 */

@Component
public class SYFCTransformScheduled {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Value("${syfc.spider.url}")
	String spiderURL;
	@Value("${syfc.transform.url}")
	String transformURL;
	@Autowired
	RestTemplate restTemplate;
	
	@Scheduled(cron = "0 0 */12 * * ?")
	private void transCommunity(){
		logger.info("transCommunity start at {}",System.currentTimeMillis());
		restTemplate.postForObject(transformURL+"/trans/community",Object.class,Object.class);
	}
	
	@Scheduled(cron = "0 0 */12 * * ?")
	private void transformBuilds(){
		logger.info("transformBuilds start at {}",System.currentTimeMillis());
		restTemplate.postForObject(transformURL+"/trans/builds",Object.class,Object.class);
	}
	
	@Scheduled(cron = "0 0 */12 * * ?")
	private void transformHouse(){
		logger.info("transformHouse start at {}",System.currentTimeMillis());
		restTemplate.postForObject(transformURL+"/trans/houses",Object.class,Object.class);
	}
	@Scheduled(cron = "0 0 */24 * * ?")
	private void transMongoCommunity(){
		logger.info("transMongoCommunity start at {}",System.currentTimeMillis());
		restTemplate.postForObject(transformURL+"/trans/mongo/community",Object.class,Object.class);
	}

	@Scheduled(cron = "0 0 */24 * * ?")
	private void transformMongoBuilds(){
		logger.info("transformMongoBuilds start at {}",System.currentTimeMillis());
		restTemplate.postForObject(transformURL+"/trans/mongo/builds",Object.class,Object.class);
	}

	@Scheduled(cron = "0 0 */24 * * ?")
	private void transformMongoHouse(){
		logger.info("transformMongoHouse start at {}",System.currentTimeMillis());
		restTemplate.postForObject(transformURL+"/trans/mongo/houses",Object.class,Object.class);
	}
}
