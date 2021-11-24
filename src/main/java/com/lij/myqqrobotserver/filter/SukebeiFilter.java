package com.lij.myqqrobotserver.filter;

import com.lij.myqqrobotserver.dto.MessageDto;
import com.lij.myqqrobotserver.entity.Sukebei;
import com.lij.myqqrobotserver.filter.base.BaseFilter;
import com.lij.myqqrobotserver.common.util.SukebeiCawler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Celphis
 * <p>种子过滤器</p>
 */
@Component
public class SukebeiFilter extends BaseFilter {
	
	@Override
	protected void init() {
		commendStr="种子";
		
	}
	
    @Override
    protected MessageDto checkMessage(MessageDto inDto) {
        String rawMessage = inDto.rawMessage;
        String regex = "^种子\\s(.)*$";
        if (!rawMessage.matches(regex)) {
            return null;
        }
        StringBuilder msg = new StringBuilder();
        List<Sukebei> sukebeis = SukebeiCawler.getSukebei(rawMessage.replaceFirst("种子\\s*", ""));
        if (sukebeis == null || sukebeis.size() == 0) {
            return parseMessage(inDto, "没有检索到结果");
        }
        for (Sukebei sukebei : sukebeis) {
            msg.append("标题:   ").append(sukebei.getTitle()).append("\n磁链:   ").append(sukebei.getMagnet()).append("\n大小:   ").append(sukebei.getSize()).append("\n日期:   ").append(sukebei.getDate());
            if (sukebeis.indexOf(sukebei) != sukebeis.size() - 1) {
                msg.append("'\n\n");
            }
        }
        return parseMessage(inDto, msg.toString());
    }
}
