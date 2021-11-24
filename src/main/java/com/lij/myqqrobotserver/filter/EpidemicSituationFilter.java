package com.lij.myqqrobotserver.filter;

import com.lij.myqqrobotserver.dto.MessageDto;
import com.lij.myqqrobotserver.entity.epidemicsituation.EpidemicSituation;
import com.lij.myqqrobotserver.filter.base.BaseFilter;
import com.lij.myqqrobotserver.task.EpidemicSituationTask;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;


@Component
public class EpidemicSituationFilter extends BaseFilter {

	@Override
	protected void init() {
		commendStr="疫情";
		
	}
	
    @Override
    protected MessageDto checkMessage(MessageDto inDto) {
        if (inDto.rawMessage.matches("^\\s*\\S*疫情$")) {
            String name = inDto.rawMessage.replaceAll("疫情", "").replaceAll("\\s*", "");
            EpidemicSituation epidemicSituation = EpidemicSituationTask.epidemicSituation.get(name);
            if (epidemicSituation == null) {
                return parseMessage(inDto, "不支持查找" + name + "数据");
            }
            StringBuilder msg = new StringBuilder();
            msg.append("城市名：").append(epidemicSituation.name)
                    .append("\n今日感染：").append(epidemicSituation.today.confirm)
                    .append("\n现存感染：").append(epidemicSituation.total.confirm-epidemicSituation.total.dead-epidemicSituation.total.heal)
                    .append("\n累计感染：").append(epidemicSituation.total.confirm)
                    .append("\n累计死亡：").append(epidemicSituation.total.dead)
                    .append("\n死亡率：").append(epidemicSituation.total.deadRate).append("%")
                    .append("\n累计治愈：").append(epidemicSituation.total.heal)
                    .append("\n治愈率：").append(epidemicSituation.total.healRate).append("%");
            if (StringUtils.isNotBlank(epidemicSituation.total.grade)) {
                msg.append("\n风险等级：").append(epidemicSituation.total.grade);
            }
            msg.append("\n更新时间：").append(EpidemicSituationTask.updateTime);
            return parseMessage(inDto, msg.toString());
        }
        return null;
    }
}
