package com.lij.myqqrobotserver.common.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Celphis
 */
public class HttpUtil {
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    public static String doGet(String api, Map<String, Object> param) {
        return get(api + mapToParamStr(param)).getBody();
    }
    public static String doPost(String api, Map<String, Object> param,Map<String,Object> body){
        if (param==null){
            return post(api,mapToJsonStr(body)).getBody();
        }
        return post(api + mapToParamStr(param),mapToJsonStr(body)).getBody();
    }
    public static ResponseEntity<String> post(String url, String body){
        return REST_TEMPLATE.postForEntity(url,JSONObject.parse(body), String.class);
    }
    public static ResponseEntity<String> get(String url) {
        return REST_TEMPLATE.getForEntity(url, String.class);
    }

    public static String mapToParamStr(Map<String, Object> param) {
        if (param!=null&&param.size()>0){
            StringBuilder paramStrBulider = new StringBuilder("?");
            for (String key : param.keySet()) {
                if (param.get(key) != null) {
                    paramStrBulider.append(key).append("=").append(param.get(key)).append("&");
                }
            }
            paramStrBulider.deleteCharAt(paramStrBulider.lastIndexOf("&"));
            return paramStrBulider.toString();
        }
       return "";
    }
    public static String mapToJsonStr(Map<String, Object> body) {
        return body==null?"":new JSONObject(body).toJSONString();
    }
}
