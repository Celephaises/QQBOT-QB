package com.lij.myqqrobotserver.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lij.myqqrobotserver.AppRunner;
import com.lij.myqqrobotserver.common.config.GoCqHttpConfig;
import com.lij.myqqrobotserver.dto.MessageDto;
import com.lij.myqqrobotserver.filter.base.BaseFilter;

/**
 * @author Celphis
 */
@RestController
public class MessageListener {
	private static final Logger logger = LogManager.getLogger();

	@RequestMapping("/")
	public String listener(@RequestBody JSONObject body) {
		MessageDto inDto = prapareInDto(body);
		try {
			BaseFilter.prepareMessage(inDto);
			for (BaseFilter filter : AppRunner.calculateFilterList) {
				if (filter.filt(inDto.clone())) {
					break;
				}
			}
		} catch (Exception e) {
			logger.error("{}", inDto, e);
		}
		return "OK";
	}

	private MessageDto prapareInDto(JSONObject body) {
		MessageDto inDto = null;
		if (StringUtils.equals(GoCqHttpConfig.POST_TYPE_NOTICE, body.getString("post_type"))) {
			inDto = new MessageDto(body.getString("message_type"), body.getString("raw_message"),
					body.getString("message"), body.getString("user_id"), body.getString("group_id"));
			inDto.postType = GoCqHttpConfig.POST_TYPE_NOTICE;
			inDto.rawMessage = "";
			inDto.messageType = GoCqHttpConfig.MSG_TYPE_GROUP;
			inDto.targetId = body.getString("target_id");
		} else if (StringUtils.equals(GoCqHttpConfig.POST_TYPE_MESSAGE, body.getString("post_type"))) {
			inDto = new MessageDto(body.getString("message_type"), body.getString("raw_message"),
					body.getString("message"), body.getString("user_id"), body.getString("group_id"));
		}
		return inDto;
	}
}
