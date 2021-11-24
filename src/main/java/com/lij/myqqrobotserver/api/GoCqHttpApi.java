package com.lij.myqqrobotserver.api;

import com.alibaba.fastjson.JSONObject;
import com.lij.myqqrobotserver.common.consts.Const;
import com.lij.myqqrobotserver.common.util.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Celphis
 */
@Component
public class GoCqHttpApi {
    public static String goCqHttpPath;

    @Value("${go_cqhttp.listener.url}")
    public void setGoCqHttpPath(String path) {
        goCqHttpPath = path;
    }

    public static JSONObject sendPrivateMsg(String fromQQ, String fromGroup, String msg) {
        Map<String, Object> param = new HashMap<>();
        param.put("user_id", fromQQ);
        param.put("group_id", fromGroup);
        param.put("message", msg);
        return send("send_private_msg", param);
    }

    public static JSONObject sendGroupMsg(String fromGroup, String msg) {
        Map<String, Object> param = new HashMap<>();
        param.put("group_id", fromGroup);
        param.put("message", msg);
        return send("send_group_msg", param);
    }

    public static void sendGroupMsg(String msg) {
        for (int i = 0; i < Const.groupList.size(); i++) {
            sendGroupMsg(Const.groupList.getJSONObject(i).getString("group_id"), msg);
        }
    }

    public static JSONObject sendMsg(String fromQQ, String fromGroup, String msg) {
        Map<String, Object> param = new HashMap<>();
        param.put("user_id", fromQQ);
        param.put("group_id", fromGroup);
        param.put("message", msg);
        return send("send_msg", param);
    }

    public static void sendMsg(String msg) {
        for (int i = 0; i < Const.friendList.size(); i++) {
            sendMsg(Const.friendList.getJSONObject(i).getString("user_id"), null, msg);
        }
    }

    public static JSONObject getGroupMemberInfo(String fromQQ, String fromGroup) {
        Map<String, Object> param = new HashMap<>();
        param.put("user_id", fromQQ);
        param.put("group_id", fromGroup);
        return get("/get_group_member_info", param);
    }

    public static JSONObject getGroupList() {
        return send("get_group_list", null);
    }

    public static JSONObject getFriendList() {
        return send("get_friend_list", null);
    }

    public static JSONObject deleteMsg(Long messageId) {
        Map<String, Object> param = new HashMap<>();
        param.put("message_id", messageId);
        return send("delete_msg", param);
    }

    private static JSONObject send(String api, Map<String, Object> body) {
        return JSONObject.parseObject(HttpUtil.doPost(goCqHttpPath + api, null, body));
    }

    private static JSONObject get(String api, Map<String, Object> body) {
        return JSONObject.parseObject(HttpUtil.doPost(goCqHttpPath + api, null, body));
    }
}
