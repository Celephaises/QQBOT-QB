package com.lij.myqqrobotserver.filter;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import com.lij.myqqrobotserver.dto.MessageDto;
import com.lij.myqqrobotserver.filter.base.BaseFilter;

/**
 * 祖安过滤器
 */
@Component
public class ZuanFilter extends BaseFilter {
	
	@Override
	protected void init() {
		commendStr="龙门粗口|口吐芬芳";
		
	}

    @Override
    protected MessageDto checkMessage(MessageDto inDto) throws Exception {
        if (inDto.rawMessage.matches("^\\s*龙门粗口\\s*$")) {
            String text = Jsoup.connect("https://zuanbot.com/api.php?lang=zh_cn").referrer("https://zuanbot.com/").ignoreContentType(true).execute().body();
            return parseMessage(inDto, text);
        }
        if (inDto.rawMessage.matches("^\\s*口吐芬芳\\s*$")) {
            String text = Jsoup.connect("https://zuanbot.com/api.php?level=min&lang=zh_cn").ignoreContentType(true).execute().body();
            return parseMessage(inDto, text);
        }
        return null;
    }

//    if($('#chk_god').is(':checked')){
//        result = result.replace('妈','爸').replace('🐴','爹').replace('🐎','爹')
//
//        var reg = new RegExp("妈","g");
//        result = result.replace(reg,"爸");
//
//        reg = new RegExp("🐴","g");
//        result = result.replace(reg,"爹");
//
//        reg = new RegExp("🐎","g");
//        result = result.replace(reg,"爹");
//
//        reg = new RegExp("母亲","g");
//        result = result.replace(reg,"爹");
//
//        reg = new RegExp("母","g");
//        result = result.replace(reg,"公");
//
//        reg = new RegExp("你吗","g");
//        result = result.replace(reg,"你爹");
//
//        reg = new RegExp("逼","g");
//        result = result.replace(reg,"屌");
//
//        reg = new RegExp("阴道","g");
//        result = result.replace(reg,"肛门");
//
//        reg = new RegExp("处女","g");
//        result = result.replace(reg,"处男");
//
//        reg = new RegExp("她","g");
//        result = result.replace(reg,"他");
//
//
//    }
}
