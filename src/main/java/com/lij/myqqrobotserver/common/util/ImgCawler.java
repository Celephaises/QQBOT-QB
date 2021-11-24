package com.lij.myqqrobotserver.common.util;

import org.apache.tomcat.util.codec.binary.Base64;
import org.jsoup.Jsoup;

/**
 * @author Celphis
 */
public class ImgCawler {
    public static String getImgBytes(String url) {
        return getImgBytes(url,null);
    }
    public static String getImgBytes(String url,String host){
        try {
            if (url != null) {
                byte[] bytes = Jsoup.connect(url).ignoreContentType(true).header("Host", host).maxBodySize(1073741824).execute().bodyAsBytes();
                return Base64.encodeBase64String(bytes);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
