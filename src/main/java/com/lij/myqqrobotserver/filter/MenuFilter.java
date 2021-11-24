package com.lij.myqqrobotserver.filter;

import org.springframework.stereotype.Component;


import com.lij.myqqrobotserver.AppRunner;
import com.lij.myqqrobotserver.dto.MessageDto;
import com.lij.myqqrobotserver.filter.base.BaseFilter;
@Component
public class MenuFilter extends BaseFilter{

	
	@Override
	protected void init() {
		commendStr="菜单";
		order=Integer.MAX_VALUE;
	}

	@Override
	protected MessageDto checkMessage(MessageDto inDto) throws Exception {
		if (!inDto.rawMessage.matches("^菜单$")) {
            return null;
        }
		StringBuilder message=new StringBuilder();
		message.append("菜单列表:\n");
		int i=0;
		for(BaseFilter filter: AppRunner.calculateFilterList) {
		  message.append(++i).append(" -- ").append(filter.getCommendStr()).append("\n");
		}
		inDto.atFlag=false;
		return parseMessage(inDto, message.deleteCharAt(message.length()-1).toString());
	}

}
