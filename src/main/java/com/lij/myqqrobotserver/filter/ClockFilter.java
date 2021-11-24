package com.lij.myqqrobotserver.filter;

import com.lij.myqqrobotserver.dto.MessageDto;
import com.lij.myqqrobotserver.entity.Clock;
import com.lij.myqqrobotserver.filter.base.BaseFilter;
import com.lij.myqqrobotserver.service.ClockService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ClockFilter extends BaseFilter {
    @Autowired
    ClockService service;

    @Override
	protected void init() {
		commendStr="打卡|打卡记录";
		
	}
    
    @Override
    protected MessageDto checkMessage(MessageDto inDto) throws Exception {
        if (inDto.rawMessage.matches("^\\s*打卡记录\\s*\\S*\\s*$")) {
            String content = inDto.rawMessage.replaceAll("\\s*", "").replaceAll("打卡记录", "");
            List<Clock> clocks = null;
            if (StringUtils.isNotBlank(content)) {
                clocks = service.selectClockWithContent(inDto.fromQQ, inDto.fromGroup, content);
            } else {
                clocks = service.selectClock(inDto.fromQQ, inDto.fromGroup);
            }
            if (clocks != null && !clocks.isEmpty()) {
                StringBuilder msg = new StringBuilder();
                if (StringUtils.isNotBlank(content)) {
                    for (Clock clock : clocks) {
                        msg.append("\n日期：").append(clock.date);
                    }
                } else {
                    for (Clock clock : clocks) {
                        msg.append("\n日期：").append(clock.date).append("\n内容：").append(clock.content);
                    }
                }
                msg.deleteCharAt(0);
                return parseMessage(inDto, msg.toString());
            }
            return parseMessage(inDto, "暂无打卡记录");
        } else if (inDto.rawMessage.matches("^\\s*打卡统计\\s*$")) {
            List<Clock> clocks = service.selectClockContentType(inDto.fromQQ, inDto.fromGroup);
            if (clocks != null && !clocks.isEmpty()) {
                StringBuilder msg = new StringBuilder();
                for (Clock clock : clocks) {
                    msg.append("\n内容：").append(clock.content).append("         ").append("次数：").append(clock.count);
                }
                msg.deleteCharAt(0);
                return parseMessage(inDto, msg.toString());
            }
            return parseMessage(inDto, "暂无打卡记录");
        } else if (inDto.rawMessage.matches("^\\s*\\S+\\s*打卡\\s*$") || inDto.rawMessage.matches("^\\s*打卡\\s*\\S+\\s*$")) {
            String content = inDto.rawMessage.replaceAll("\\s*", "").replaceAll("打卡", "");
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int flag = service.insertClock(inDto.fromQQ, inDto.fromGroup, content, dateFormatter.format(new Date()));
            if (flag == 1) {
                return parseMessage(inDto, "打卡成功");
            } else {
                return parseMessage(inDto, "打卡失败");
            }
        }
        return null;
    }
}
