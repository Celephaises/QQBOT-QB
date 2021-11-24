package com.lij.myqqrobotserver.filter;

import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lij.myqqrobotserver.common.util.HttpUtil;
import com.lij.myqqrobotserver.common.util.ImgCawler;
import com.lij.myqqrobotserver.dto.MessageDto;
import com.lij.myqqrobotserver.filter.base.BaseFilter;
import com.lij.myqqrobotserver.task.ZaoBaoTask;

@Component
public class ZaoBaoFilter extends BaseFilter {

	@Override
	protected void init() {
		commendStr = "今日早报";

	}

	@Override
	protected MessageDto checkMessage(MessageDto inDto) throws Exception {
		if (!inDto.rawMessage.matches("^\\s*今日早报\\s*$")) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		String zaoBaoMessage;
		if (hour < 9 || (hour == 9 && minute <= 30)) {
			String zaoBaoUrl = JSONObject
					.parseObject(HttpUtil.doGet("https://v2.alapi.cn/api/zaobao?token=dnpELkcLnMqzmsTo", null))
					.getJSONObject("data").getString("image").replaceAll("!/format/webp", "");
			zaoBaoMessage="[CQ:image,file=base64://" + ImgCawler.getImgBytes(zaoBaoUrl) + "]" ;
		} else {
			zaoBaoMessage = ZaoBaoTask.zaoBaoMessage;
		}
		inDto.atFlag = false;
		return parseMessage(inDto, zaoBaoMessage);
	}

}
