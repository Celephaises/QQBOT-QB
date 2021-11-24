package com.lij.myqqrobotserver;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.lij.myqqrobotserver.api.GoCqHttpApi;
import com.lij.myqqrobotserver.common.browser.Browser;
import com.lij.myqqrobotserver.common.consts.Const;
import com.lij.myqqrobotserver.filter.CardFilter;
import com.lij.myqqrobotserver.filter.base.BaseFilter;
import com.lij.myqqrobotserver.task.base.BaseTask;
import com.microsoft.playwright.Playwright;

@Component
public class AppRunner implements ApplicationRunner, DisposableBean {
	private static final Logger log = LogManager.getLogger();
	@Autowired
	private Map<String, BaseTask> calculateTaskMap;
	@Autowired
	private Map<String, BaseFilter> calculateFilterMap;

	public static List<BaseFilter> calculateFilterList;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		initMenu();
		Const.groupList = GoCqHttpApi.getGroupList().getJSONArray("data");
		log.info("群列表加载完成");
		Const.friendList = GoCqHttpApi.getFriendList().getJSONArray("data");
		log.info("好友列表加载完成");
		for (BaseTask task : calculateTaskMap.values()) {
			task.excute();
			log.info("{} 定时器加载完成", task.getClass().getCanonicalName());
		}
		CardFilter.cardHtml = IOUtils.toString(new ClassPathResource("static/card1.html").getInputStream(),
				StandardCharsets.UTF_8);
		Browser.playwright = Playwright.create();
		Browser.browser = Browser.playwright.chromium().launch();
		log.info("模拟浏览器加载完成");
		Const.fortunes = JSONArray.parseArray(IOUtils
				.toString(new ClassPathResource("static/fortune.json").getInputStream(), StandardCharsets.UTF_8));
	}

	@Override
	public void destroy() throws Exception {
		Browser.browser.close();
		Browser.playwright.close();
		log.info("模拟浏览器销毁完成");
	}

	private void initMenu() {
		calculateFilterList=new ArrayList<BaseFilter>();
		for (Entry<String, BaseFilter> filterEntry : calculateFilterMap.entrySet()) {
			calculateFilterList.add(filterEntry.getValue());
		}
		Collections.sort(calculateFilterList);
	}
}
