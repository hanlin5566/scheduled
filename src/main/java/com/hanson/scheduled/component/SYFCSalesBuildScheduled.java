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
 * * 1.每日爬取列表，对比detail http://www.syfc.com.cn/work/xjlp/new_building.jsp
 * POST http://192.168.1.13:9000/syfc/build/incrementNewBuildDetail
 * syfc_new_build_detail -->全量&最新
 * 2.生成采集列表
 * POST http://localhost:9000/syfc/build/initTodayNewBuildDetail
 * syfc_new_build_detail_2019_03_08 -->当日，历史
 * 3.采集列表
 * http://localhost:9000/syfc/build/collectNewBuildDetail
 * syfc_new_build_detail_2019_03_08 -->当日，历史
 * 更新syfc_new_build_detail -->全量&最新
 * 4.生成house的task
 * http://192.168.1.13:9000/syfc/build/transformBuildHouseTask
 * 5.采集house detail
 * http://192.168.1.13:9000/syfc/build/collectNewHouseDetail
 */

@Component
public class SYFCSalesBuildScheduled {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Value("${syfc.spider.url}")
	String spiderURL;
	@Value("${syfc.transform.url}")
	String transformURL;
	@Autowired
	RestTemplate restTemplate;
	
	/**
	 * 1.每日爬取列表，对比detail http://www.syfc.com.cn/work/xjlp/new_building.jsp
	 * POST http://192.168.1.13:9000/syfc/build/incrementNewBuildDetail
	 * syfc_new_build_detail -->全量&最新
	 * 每8小时执行一次
	 **/
	@Scheduled(cron = "0 0 */8 * * ?")
	private void incrementNewBuildDetail() throws InterruptedException {
		logger.info("incrementNewBuildDetail start at"+System.currentTimeMillis());
		restTemplate.postForObject(spiderURL+"/syfc/build/incrementNewBuildDetail",Object.class,Object.class);
	}
	
	/**
	 * 2.生成采集列表
	 * POST http://localhost:9000/syfc/build/initTodayNewBuildDetail
	 * syfc_new_build_detail_2019_03_08 -->当日，历史
	 * 每天01点执行一次
	 **/
	@Scheduled(cron = "0 0 1 * * ?")
	private void initTodayNewBuildDetail() throws InterruptedException {
		logger.info("initTodayNewBuildDetail start at"+System.currentTimeMillis());
		restTemplate.postForObject(spiderURL+"/syfc/build/initTodayNewBuildDetail",Object.class,Object.class);
	}
	
	/**
	 * 3.采集列表
	 * http://localhost:9000/syfc/build/collectNewBuildDetail
	 * syfc_new_build_detail_2019_03_08 -->当日，历史
	 * 更新syfc_new_build_detail -->全量&最新
	 **/
	@Scheduled(cron = "0 0 */6 * * ?")
	private void collectNewBuildDetail() throws InterruptedException {
		logger.info("collectNewBuildDetail start at"+System.currentTimeMillis());
		restTemplate.postForObject(spiderURL+"/syfc/build/collectNewBuildDetail",Object.class,Object.class);
	}
	
	/**
	 * 4.生成house的task
	 * http://192.168.1.13:9000/syfc/build/transformBuildHouseTask
	 **/
	@Scheduled(cron = "0 0 */8 * * ?")
	private void transformBuildHouseTask() throws InterruptedException {
		logger.info("transformBuildHouseTask start at"+System.currentTimeMillis());
		restTemplate.postForObject(spiderURL+"/syfc/build/transformBuildHouseTask",Object.class,Object.class);
	}
	
	/**
	 * 5.采集house的task
	 * http://192.168.1.13:9000/syfc/build/transformBuildHouseTask
	 **/
	@Scheduled(cron = "0 0 */4 * * ?")
	private void collectNewHouseDetail() throws InterruptedException {
		logger.info("collectNewHouseDetail start at"+System.currentTimeMillis());
		restTemplate.postForObject(spiderURL+"/syfc/build/collectNewHouseDetail",Object.class,Object.class);
	}
}
