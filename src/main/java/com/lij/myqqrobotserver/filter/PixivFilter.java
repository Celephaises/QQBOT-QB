package com.lij.myqqrobotserver.filter;

import com.lij.myqqrobotserver.dto.MessageDto;
import com.lij.myqqrobotserver.entity.PixivImg;
import com.lij.myqqrobotserver.filter.base.BaseFilter;
import com.lij.myqqrobotserver.service.PixivImgService;
import com.lij.myqqrobotserver.common.thread.ThreadPool;
import com.lij.myqqrobotserver.common.util.PixivCawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author Celphis
 * <p>色图过滤器</p>
 */
@Component
public class PixivFilter extends BaseFilter {
	
	@Override
	protected void init() {
		commendStr="美图|色图|涩图";
		
	}
	
    @Autowired
    private PixivImgService pixivImgService;
    @Override
    public MessageDto checkMessage(MessageDto inDto) {
        String regax = "^(美图|色图|涩图)(|(\\s+\\S*|\\s+[0-9]*|\\s+\\S*\\s+[0-9]*))$";
        String rawMessage = inDto.rawMessage;
        if (!rawMessage.matches(regax)) {
            return null;
        }
        String order = rawMessage.replaceAll("\\s+\\S*\\s*[0-9]*", "");
        String tag = rawMessage.replaceFirst("(美图|色图|涩图)\\s*", "").replaceAll("\\s*[0-9]*", "");
        String lengthStr = rawMessage.replaceAll("\\S*\\s+", "").replaceAll("[^0-9]", "");
        int length = 1;
        if (!"".equals(lengthStr)) {
            length = Math.min(Integer.parseInt(lengthStr), 10);
        }
        List<PixivImg> imgs = null;
        switch (order) {
            case "美图":
                imgs = pixivImgService.getPixivImgRandomList(tag, 0, length);
                break;
            case "色图":
                imgs = pixivImgService.getPixivImgRandomList(tag, 1, length);
                break;
            case "涩图":
                imgs = pixivImgService.getPixivImgRandomList(tag, 2, length);
                break;
            default:
                break;
        }
        if (imgs == null || imgs.size() == 0) {
            return parseMessage(inDto, "你的XP太怪了，检索不到信息");
        }
        StringBuilder msg = new StringBuilder();
        CountDownLatch countDown = new CountDownLatch(imgs.size());
        ConcurrentHashMap<Long, String> resultMap = new ConcurrentHashMap<>();
        for (PixivImg img : imgs) {
            ThreadPool.getPool().execute(() -> {
                try {
                    String base64Str = PixivCawler.getImgBytes(img);
                    if (base64Str != null) {
                        resultMap.put(img.getPid(), "base64://" + base64Str);
                    }
                } finally {
                    countDown.countDown();
                }
            });
        }
        try {
            countDown.await();
        } catch (InterruptedException e) {
            logger.error("", e);
        }
        for (Long pid : resultMap.keySet()) {
            msg.append("作品编号:").append(pid).append("[CQ:image,file=").append(resultMap.get(pid)).append("]");
        }
        inDto.deleteMSgFlag = true;
        return parseMessage(inDto, msg.toString());
    }
}
