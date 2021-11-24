package com.lij.myqqrobotserver.filter;

import com.lij.myqqrobotserver.common.config.ImgApiConfig;
import com.lij.myqqrobotserver.dto.MessageDto;
import com.lij.myqqrobotserver.filter.base.BaseFilter;
import com.lij.myqqrobotserver.common.thread.ThreadPool;
import com.lij.myqqrobotserver.common.util.ImgCawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/**
 * @author Celphis
 */
@Component
public class ImgApiFilter extends BaseFilter {
    @Autowired
    private ImgApiConfig imgApiConfig;

    @Override
	protected void init() {
		commendStr="随机图片";
		
	}
    
    @Override
    protected MessageDto checkMessage(MessageDto inDto) throws InterruptedException {
        String rawMessage = inDto.rawMessage;
        for (String key : imgApiConfig.getPictureMap().keySet()) {
            String regax = "^" + key + "\\s*[0-9]*$";
            if (rawMessage.matches(regax)) {
                String lengthStr = rawMessage.replaceAll(key, "").replaceAll("[^0-9]", "");
                int length = 1;
                if (!"".equals(lengthStr)) {
                    length = Math.min(Integer.parseInt(lengthStr), 10);
                }
                StringBuilder msg = new StringBuilder();
                CountDownLatch countDown = new CountDownLatch(length);
                ConcurrentLinkedQueue<String> resultQueue = new ConcurrentLinkedQueue<>();
                for (int i = 0; i < length; i++) {
                    ThreadPool.getPool().execute(() -> {
                        try {
                            String base64Str = ImgCawler.getImgBytes(imgApiConfig.getPictureMap().get(key));
                            if (base64Str == null) {
                                resultQueue.offer("65a2f3a99440ae07b9fb3bff606c1eac.image");
                            } else {
                                resultQueue.offer("base64://" + base64Str);
                            }
                        } finally {
                            countDown.countDown();
                        }
                    });
                }
                countDown.await();
                while (!resultQueue.isEmpty()) {
                    msg.append("[CQ:image,file=").append(resultQueue.poll()).append("]");
                }
                return parseMessage(inDto, msg.toString());
            }
        }
        return null;
    }
}
