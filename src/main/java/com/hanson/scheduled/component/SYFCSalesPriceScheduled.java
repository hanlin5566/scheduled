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
 * 每次从22页开始爬取，爬取三页，比对第三方Id如果没有则插入
 * 1.每日爬取列表，对比price_list http://218.25.83.4:7003/newbargain/download/findys/ys_info.jsp?kfs=&xmxq=&ldz=&ysid=&yzmcode=ssss&flagcx=1
 * POST http://192.168.1.13:9000/syfc/salesPirce/incrementtSalesPrice
 * 2.同步sales_price_list
 * POST http://192.168.1.13:9000/syfc/salesPirce/syncSalesPriceDetail
 * 3.每日爬取售价详情
 * POST http://192.168.1.13:9000/syfc/salesPirce/collectSalesPriceDetail
 */

@Component
public class SYFCSalesPriceScheduled {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Value("${syfc.spider.url}")
	String spiderURL;
	@Value("${syfc.transform.url}")
	String transformURL;
	@Autowired
	RestTemplate restTemplate;
	
	/**
	 * 1.每日爬取列表，对比price_list http://218.25.83.4:7003/newbargain/download/findys/ys_info.jsp?kfs=&xmxq=&ldz=&ysid=&yzmcode=ssss&flagcx=1
	 * POST http://192.168.1.13:9000/syfc/salesPirce/incrementtSalesPrice
	 * 每2小时执行一次
	 **/
	@Scheduled(cron = "0 0 */2 * * ?")
	private void incrementtSalesPrice() throws InterruptedException {
		logger.info("incrementtSalesPrice start at {}",System.currentTimeMillis());
		restTemplate.postForObject(spiderURL+"/syfc/salesPirce/incrementtSalesPrice",Object.class,Object.class);
	}
	
	/**
	 * 2.同步sales_price_list
	 * POST http://192.168.1.13:9000/syfc/salesPirce/syncSalesPriceDetail
	 * 每三小时执行一次
	 **/
	@Scheduled(cron = "0 0 */3 * * ?")
	private void syncSalesPriceDetail() throws InterruptedException {
		logger.info("syncSalesPriceDetail start at {}",System.currentTimeMillis());
		restTemplate.postForObject(spiderURL+"/syfc/salesPirce/syncSalesPriceDetail",Object.class,Object.class);
	}
	
	/**
	 * 3.每日爬取售价详情
	 * POST http://192.168.1.13:9000/syfc/salesPirce/collectSalesPriceDetail
	 * 每六小时执行一次
	 **/
	@Scheduled(cron = "0 0 */4 * * ?")
	private void collectSalesPriceDetail() throws InterruptedException {
		logger.info("collectSalesPriceDetail start at {}",System.currentTimeMillis());
		restTemplate.postForObject(spiderURL+"/syfc/build/collectSalesPriceDetail",Object.class,Object.class);
	}
}
