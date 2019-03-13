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
public class SYFCSalesNoScheduled {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Value("${syfc.spider.url}")
	String spiderURL;
	@Value("${syfc.transform.url}")
	String transformURL;
	//每5小时运行一次
	@Autowired
	RestTemplate restTemplate;
	
	long minute = 1000*60;
	@Scheduled(cron = "0 0 */6 * * ?")
	private void process() throws InterruptedException {
		logger.info("start salesno at{}",System.currentTimeMillis());
		/**
		 * 1.每日爬取列表，比对detail。http://www.syfc.com.cn/work/ysxk/query_xukezheng.jsp
		 * POST http://localhost:9000/syfc/salesNo/incrementSalesNo
		 * 2.新增数据清洗到sales_no_detail 
		 * POST http://localhost:9100/salesNo/increment
		 * 3.继续爬取salesNODetail
		 * http://localhost:9000/syfc/salesNo/collectDetail
		 * 4.清洗到mysql
		 * http://192.168.1.13:9100/salesNo/detail
		 */
		restTemplate.postForObject(spiderURL+"/syfc/salesNo/incrementSalesNo",Object.class,Object.class);
		Thread.sleep(minute*10);
		restTemplate.postForObject(transformURL+"/salesNo/increment",Object.class,Object.class);
		Thread.sleep(minute*5);
		restTemplate.postForObject(spiderURL+"/syfc/salesNo/collectDetail",Object.class,Object.class);
		Thread.sleep(minute*10);
		restTemplate.postForObject(transformURL+"/salesNo/detail",Object.class,Object.class);
		Thread.sleep(minute*5);
	}
}
