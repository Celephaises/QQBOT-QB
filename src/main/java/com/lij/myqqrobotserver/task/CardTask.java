package com.lij.myqqrobotserver.task;

import com.alibaba.fastjson.JSONObject;
import com.lij.myqqrobotserver.task.base.BaseTask;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class CardTask extends BaseTask {
    public static ConcurrentHashMap<String, JSONObject> cardMap=new ConcurrentHashMap<>();

    @Override
    public void task() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getTimeInMillis() < System.currentTimeMillis() ? calendar.get(Calendar.DAY_OF_YEAR) + 1 : calendar.get(Calendar.DAY_OF_YEAR));
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            cardMap=new ConcurrentHashMap<>();
            logger.info("打卡记录初始化");
        },calendar.getTimeInMillis()-System.currentTimeMillis(), 60 * 60 * 24 * 1000, TimeUnit.MILLISECONDS);
    }
}
