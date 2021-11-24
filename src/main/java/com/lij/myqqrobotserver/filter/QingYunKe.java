package com.lij.myqqrobotserver.filter;

import com.alibaba.fastjson.JSONObject;
import com.lij.myqqrobotserver.common.config.GoCqHttpConfig;
import com.lij.myqqrobotserver.dto.MessageDto;
import com.lij.myqqrobotserver.filter.base.BaseFilter;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class QingYunKe extends BaseFilter {
	@Override
	protected void init() {
		commendStr="聊天API";
		order=-10;
		
	}
	
    @Override
    protected MessageDto checkMessage(MessageDto inDto) throws IOException {
        if (inDto.toRobotFlag || GoCqHttpConfig.MSG_TYPE_PRIVATE.equals(inDto.messageType) || inDto.rawMessage.matches("^\\s*\\S*\\s*丘比\\s*\\S*\\s*$")) {
            String text = Jsoup.connect("https://api.qingyunke.com/api.php?key=free&appid=0&msg=" + inDto.rawMessage.replaceAll("丘比", "").replaceAll("\\s*", "")).ignoreContentType(true).execute().body();
            if (text != null) {
                JSONObject object = JSONObject.parseObject(text);
                if (object.getInteger("result") == 0) {
                    return parseMessage(inDto, object.getString("content").replaceAll("\\{br\\}", "\n").replaceAll("\\{\\S*\\}", ""));
                }
            }
        }
        return null;
    }
}
