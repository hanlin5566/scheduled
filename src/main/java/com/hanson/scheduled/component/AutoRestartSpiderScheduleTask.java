package com.hanson.scheduled.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

/**
 * @author Hanson
 * create on 2019年2月10日
 */

//@Component
@Deprecated
public class AutoRestartSpiderScheduleTask  {
	Logger logger = LoggerFactory.getLogger(this.getClass());
    private  static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    //休眠5秒，等待關閉
    long shut_down_sleep = 1000*2;
    //每次執行周期
    private int connectTimeout = 1000*10;
	private int readTimeout = 1000*10;
	
	@Scheduled(fixedRate = 1000*60*5)
    public void reportCurrentTime() throws IOException{
    	//關閉程序
		logger.info("当前时间：" + dateFormat.format(new Date()));
    	SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();  
		requestFactory.setConnectTimeout(connectTimeout);// 设置超时  
		requestFactory.setReadTimeout(readTimeout);  
    	try {
    		logger.info("尝试关闭服务");
    		RestTemplate restTemplate = new RestTemplate(requestFactory);  
    		String shutdownURL = "http://localhost:9000/shutdown";
    		String json = restTemplate.postForEntity(shutdownURL, "", String.class).getBody();
    		System.out.println(json);
    		Runtime runtime = Runtime.getRuntime();
    		Process exec = runtime.exec("jps");
    		//直到读完为止  
	        InputStream is = exec.getInputStream();  
            //用一个读输出流类去读  
            //用缓冲器读行  
            BufferedReader br=new BufferedReader( new InputStreamReader(is,"UTF-8"));  
            String line=null;  
            //直到读完为止  
            while((line=br.readLine())!=null)  
            {  
            	System.out.println(line);
            	if(line.indexOf("spider.jar")>=0) {
            		String taskId = line.replaceAll("spider.jar", "");
            		taskId= taskId.trim();
            		Runtime kill = Runtime.getRuntime();
            		kill.exec("tskill "+taskId);
            	}
            }    
		} catch (Exception e) {
			logger.error("spider服務未啓動");
		}
    	Process exec = null;
    	try {
    		Thread.sleep(shut_down_sleep);
    		Runtime runtime = Runtime.getRuntime();
    		exec = runtime.exec("java -jar D:\\mine\\workspace\\spider\\target\\spider.jar");
    		//取得命令结果的输出流  
            InputStream is = exec.getInputStream();  
            //用一个读输出流类去读  
            //用缓冲器读行  
            BufferedReader br=new BufferedReader( new InputStreamReader(is,"UTF-8"));  
            String line=null;  
            //直到读到成功为止  
            while((line=br.readLine())!=null)  
            {  
            	System.out.println(line);
            	if(line.indexOf("JVM running")>=0) {
            		logger.info("*************************启动成功**********************");
            		break;
            	}
            	/**
            	 * APPLICATION FAILED TO START
            	 */
            	if(line.indexOf("APPLICATION FAILED TO START")>=0) {
            		logger.info("*************************启动失败**********************");
            		logger.info("尝试关闭服务");
            		RestTemplate restTemplate = new RestTemplate(requestFactory);  
            		String shutdownURL = "http://localhost:9000/shutdown";
            		String json = restTemplate.postForEntity(shutdownURL, "", String.class).getBody();
            		System.out.println(json);
            		break;
            	}
            		
            }  
		} catch (Exception e) {
			e.printStackTrace();
		}
        try {
			logger.info("采集預售許可證詳情列表");
			//采集 預售證詳情 http://localhost:9000/syfc/collectDetail
			RestTemplate collectDetailURLRestTemplate = new RestTemplate();  
	        String collectDetailURL = "http://localhost:9000/syfc/collectDetail";
	        collectDetailURLRestTemplate.postForEntity(collectDetailURL, "", String.class).getBody();
	        logger.info("采集售價列表");
	        //采集 售價列表 http://localhost:9000/syfc/collectPriceList
	        RestTemplate collectPriceListRestTemplate = new RestTemplate();  
	        String collectPriceListURL = "http://localhost:9000/syfc/collectPriceList";
	        collectPriceListRestTemplate.postForEntity(collectPriceListURL, "", String.class).getBody();
	        //直到读完为止  
	        InputStream is = exec.getInputStream();  
            //用一个读输出流类去读  
            //用缓冲器读行  
            BufferedReader br=new BufferedReader( new InputStreamReader(is,"UTF-8"));  
            String line=null;  
            //直到读完为止  
            while((line=br.readLine())!=null)  
            {  
            	System.out.println(line);
            }    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
