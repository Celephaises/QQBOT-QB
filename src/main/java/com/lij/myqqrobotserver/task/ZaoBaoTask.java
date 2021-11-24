package com.lij.myqqrobotserver.task;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lij.myqqrobotserver.api.GoCqHttpApi;
import com.lij.myqqrobotserver.common.util.HttpUtil;
import com.lij.myqqrobotserver.common.util.ImgCawler;
import com.lij.myqqrobotserver.task.base.BaseTask;

@Component
public class ZaoBaoTask extends BaseTask {
	public static String zaoBaoUrl;
	public static String zaoBaoMessage;
	@Override
	public void task() throws Exception {
		zaoBaoUrl = JSONObject
				.parseObject(HttpUtil.doGet("https://v2.alapi.cn/api/zaobao?token=dnpELkcLnMqzmsTo", null))
				.getJSONObject("data").getString("image").replaceAll("!/format/webp", "");
		zaoBaoMessage="[CQ:image,file=base64://" + ImgCawler.getImgBytes(zaoBaoUrl) + "]" ;
		logger.info("早报初始化完成  {}",zaoBaoUrl);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 7);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.DAY_OF_YEAR,
				calendar.getTimeInMillis() < System.currentTimeMillis() ? calendar.get(Calendar.DAY_OF_YEAR) + 1
						: calendar.get(Calendar.DAY_OF_YEAR));
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
			try {
				int i=0;
				while(i++<20) {
					try {
						String tempUrl=JSONObject
								.parseObject(HttpUtil.doGet("https://v2.alapi.cn/api/zaobao?null))
								.getJSONObject("data").getString("image").replaceAll("!/format/webp", "");
						if(tempUrl==null||StringUtils.equals(tempUrl, zaoBaoUrl)) {
							Thread.sleep(60000);
							continue;
						}
						zaoBaoUrl=tempUrl;
						zaoBaoMessage="[CQ:image,file=base64://" + ImgCawler.getImgBytes(zaoBaoUrl) + "]" ;
						break;
					}catch(Exception e) {
						logger.info("定时任务异常  :",e);
						continue;
					}
				}
				logger.info("早报初始化完成  {}",zaoBaoUrl);
			} catch (Exception e) {
				logger.info("定时任务异常  :",e);
			}
		}, calendar.getTimeInMillis() - System.currentTimeMillis(), 60 * 60 * 24 * 1000, TimeUnit.MILLISECONDS);
		calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 9);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.DAY_OF_YEAR,
				calendar.getTimeInMillis() < System.currentTimeMillis() ? calendar.get(Calendar.DAY_OF_YEAR) + 1
						: calendar.get(Calendar.DAY_OF_YEAR));
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
			GoCqHttpApi.sendGroupMsg(zaoBaoMessage);
			GoCqHttpApi.sendMsg(zaoBaoMessage);
			logger.info("早报推送完成");
		}, calendar.getTimeInMillis() - System.currentTimeMillis(), 60 * 60 * 24 * 1000, TimeUnit.MILLISECONDS);
	}
}
