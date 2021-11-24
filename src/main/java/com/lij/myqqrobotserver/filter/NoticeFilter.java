package com.lij.myqqrobotserver.filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lij.myqqrobotserver.common.config.GoCqHttpConfig;
import com.lij.myqqrobotserver.dto.MessageDto;
import com.lij.myqqrobotserver.filter.base.BaseFilter;

@Component
public class NoticeFilter extends BaseFilter{
	@Autowired
	GoCqHttpConfig config;
	@Override
	protected void init() {
		commendStr="戳一戳";
		order=10;
	}

	@Override
	protected MessageDto checkMessage(MessageDto inDto) throws Exception {
		if(StringUtils.equals(GoCqHttpConfig.POST_TYPE_NOTICE, inDto.postType)&&StringUtils.equals(inDto.targetId,config.getSelfQQ())) {
			inDto.atFlag=false;
			return parseMessage(inDto, "请不要戳丘比 >_<");
		}
		return null;
	}

}
