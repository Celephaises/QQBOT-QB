package com.lij.myqqrobotserver.filter;

import com.alibaba.fastjson.JSONObject;
import com.lij.myqqrobotserver.api.GoCqHttpApi;
import com.lij.myqqrobotserver.common.browser.Browser;
import com.lij.myqqrobotserver.common.config.GoCqHttpConfig;
import com.lij.myqqrobotserver.common.consts.Const;
import com.lij.myqqrobotserver.dto.MessageDto;
import com.lij.myqqrobotserver.filter.base.BaseFilter;
import com.lij.myqqrobotserver.task.CardTask;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Component
public class CardFilter extends BaseFilter {
    public static String cardHtml;
    @Override
	protected void init() {
		commendStr="签到";
		order=3;
	}
    
    @Override
    protected MessageDto checkMessage(MessageDto inDto) throws Exception {
        if (!inDto.rawMessage.matches("^签到$")) {
            return null;
        }
        Page page = null;
        try {
            JSONObject fortune = CardTask.cardMap.get(inDto.fromQQ);
            String cardFlag = "[今天已经签到过了哦]";
            String point = "0";
            if (fortune == null) {
                fortune = Const.fortunes.getJSONObject(new Random().nextInt(Const.fortunes.size()));
                CardTask.cardMap.put(inDto.fromQQ, fortune);
                cardFlag = "";
                point = String.valueOf(new Random().nextInt(20) + 1);
            }
            String html = cardHtml;
            page = Browser.browser.newPage();
            Calendar calendar = Calendar.getInstance();
            String greetings = getGreetings(calendar);
            if (GoCqHttpConfig.MSG_TYPE_GROUP.equals(inDto.messageType)) {
                String card = GoCqHttpApi.getGroupMemberInfo(inDto.fromQQ, inDto.fromGroup).getJSONObject("data").getString("card");
                if (card.length() > 10) {
                    greetings = greetings + card.substring(0, 12) + "...";
                } else {
                    greetings = greetings + card;
                }
            }
            page.setContent(html.replaceAll("\\{GREETINGS\\}", greetings)
                    .replaceAll("\\{DATE_TIME\\}", new SimpleDateFormat("yyyy/MM/dd").format(new Date()))
                    .replaceAll("\\{LUCK_STATUE\\}", fortune.getString("FORTUNE_SUMMARY"))
                    .replaceAll("\\{STARS\\}", fortune.getString("LUCKY_STAR"))
                    .replaceAll("\\{COMMENT\\}", fortune.getString("SIGN_TEXT"))
                    .replaceAll("\\{RESOLVE\\}", fortune.getString("UN_SIGN_TEXT"))
                    .replaceAll("\\{CARD_FLAG\\}", cardFlag)
                    .replaceAll("\\{POINTS\\}", point + cardFlag)
                    .replaceAll("\\{HEAD_URL\\}", "https://q1.qlogo.cn/g?b=qq&nk=QQ_NUM&s=100&t=".replaceAll("QQ_NUM", inDto.fromQQ)));
            ElementHandle card = page.querySelector("#card");
            byte[] bytes = card.screenshot();
            MessageDto messageDto = new MessageDto(inDto, "[CQ:image,file=base64://" + Base64.encodeBase64String(bytes) + "]");
            messageDto.atFlag = false;
            return messageDto;
        } finally {
            if (page != null) {
                page.close();
            }
        }
    }

    private String getGreetings(Calendar calendar) {
        String greetings;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour < 3 || hour >= 18) {
            greetings = "晚上好,";
        } else if (hour < 11) {
            greetings = "早上好,";
        } else if (hour < 13) {
            greetings = "中午好,";
        } else {
            greetings = "下午好,";
        }
        return greetings;
    }
}
