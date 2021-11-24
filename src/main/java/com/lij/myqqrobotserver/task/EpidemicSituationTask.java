package com.lij.myqqrobotserver.task;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lij.myqqrobotserver.entity.epidemicsituation.EpidemicSituation;
import com.lij.myqqrobotserver.entity.epidemicsituation.Today;
import com.lij.myqqrobotserver.entity.epidemicsituation.Total;
import com.lij.myqqrobotserver.task.base.BaseTask;

@Component
public class EpidemicSituationTask extends BaseTask {
    public static String updateTime;
    public static HashMap<String, EpidemicSituation> epidemicSituation = new HashMap<>();
    public static HashMap<String, EpidemicSituation> epidemicSituationTemp = new HashMap<>();

    @Override
    public void task() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::initEpidemicSituation, 0, 1000 * 60 * 60, TimeUnit.MILLISECONDS);
    }

    private void initEpidemicSituation() {
        try {
            JSONObject data = JSONObject.parseObject(Jsoup.connect("https://view.inews.qq.com/g2/getOnsInfo?name=disease_h5").ignoreContentType(true).execute().body()).getJSONObject("data");
            updateTime=data.getString("lastUpdateTime");
            analysizeAreaTree(data.getJSONArray("areaTree"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        epidemicSituation.clear();
        epidemicSituation.putAll(epidemicSituationTemp);
        epidemicSituationTemp.clear();
    }

    private void analysizeAreaTree(JSONArray tree) {
        int size = tree.size();
        for (int i = 0; i < size; i++) {
            putMap(tree.getJSONObject(i));
        }
    }

    private void putMap(JSONObject children) {
        if (StringUtils.equals("境外输入", children.getString("name")) || StringUtils.equals("地区待确认", children.getString("name"))) {
            return;
        }
        EpidemicSituation epidemicSituation = new EpidemicSituation();
        epidemicSituation.name = children.getString("name");
        epidemicSituation.today = new Today(children.getJSONObject("today").getInteger("confirm"));
        epidemicSituation.total = JSONObject.parseObject(children.getJSONObject("total").toJSONString(), Total.class);
        epidemicSituationTemp.put(epidemicSituation.name, epidemicSituation);
        if (children.getJSONArray("children") != null && children.getJSONArray("children").size() > 0) {
            analysizeAreaTree(children.getJSONArray("children"));
        }
    }
}
