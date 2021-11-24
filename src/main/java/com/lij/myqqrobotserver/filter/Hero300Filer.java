package com.lij.myqqrobotserver.filter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lij.myqqrobotserver.dto.MessageDto;
import com.lij.myqqrobotserver.filter.base.BaseFilter;
import com.lij.myqqrobotserver.common.util.HttpUtil;
import org.springframework.stereotype.Component;

/**
 * @author Celphis
 */
@Component
public class Hero300Filer extends BaseFilter {
	
	@Override
	protected void init() {
		commendStr="300";
		
	}
	
    @Override
    protected MessageDto checkMessage(MessageDto inDto) {
        String regax = "^300\\s+\\S+$";
        String rawMessage = inDto.rawMessage;
        if (!rawMessage.matches(regax)) {
            return null;
        }
        String name = rawMessage.replaceAll("300\\s+", "");
        JSONObject result = JSONObject.parseObject(HttpUtil.get("https://300report.jumpw.com/api/getrole?name=" + name).getBody());
        JSONObject role = result.getJSONObject("Role");
        if (role == null) {
            return parseMessage(inDto, "角色 [" + name + "] 不存在");
        }
        String roleName = role.getString("RoleName");
        String roleLev = role.getString("RoleLevel");
        Integer winCount = role.getInteger("WinCount");
        Integer matchCount = role.getInteger("MatchCount");
        String updateTime = role.getString("UpdateTime");
        String shenlv = String.valueOf(winCount.floatValue() * 100 / matchCount.floatValue()).substring(0, 5) + "%";
        String tuanfeng = null;
        JSONArray rankArray = result.getJSONArray("Rank");
        if (rankArray != null) {
            for (int i = 0; i < rankArray.size(); i++) {
                if ("团队实力排行".equals(rankArray.getJSONObject(i).getString("RankName"))) {
                    tuanfeng = rankArray.getJSONObject(i).getString("Value");
                }
            }
        }
        return parseMessage(inDto, "角色名：" + roleName + "\n角色等级：" + roleLev + "\n胜率：" + shenlv + "\n团分：" + tuanfeng + "\n更新日期：" + updateTime);
    }
}
