package com.lij.myqqrobotserver.common.util;

import com.alibaba.fastjson.JSON;
import com.lij.myqqrobotserver.common.consts.Const;
import com.lij.myqqrobotserver.entity.PixivImg;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.codec.binary.Base64;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * @author Celphis
 */
@Component
public class PixivCawler {
    @Value("${pixiv.quality}")
    private int pixivQuality;
    private static String quality;

    @PostConstruct
    public void init() {
        switch (pixivQuality) {
            case 0:
                quality = "original";
                break;
            case 2:
                quality = "mini";
                break;
            case 3:
                quality = "small";
                break;
            case 1:
            default:
                quality = "regular";
                break;

        }
    }

    public static String getImgBytes(PixivImg img) {
        try {
            String url = PixivCawler.getImgUrl(img);
            if (url != null) {
                byte[] bytes = Jsoup.connect(url).ignoreContentType(true).proxy(Const.pixivProxy).maxBodySize(1073741824)
                        .referrer("https://www.pixiv.net/artworks/" + img.getPid()).execute().bodyAsBytes();
//                return Base64.encodeBase64String(compressPicForScale(bytes, 200));
                return Base64.encodeBase64String(bytes);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static String getImgUrl(PixivImg img) throws Exception {
        Connection.Response res = null;
        for (int i = 0; i < 3; i++) {
            try {
                res = Jsoup.connect(img.getUrl()).proxy(Const.pixivProxy).timeout(5000).header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8").header("Accept-Encoding", "gzip, deflate, sdch").header("Accept-Language", "zh-CN,zh;q=0.8").header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36").execute();
                break;
            } catch (ConnectException e) {// cookie错误将会超时, 但是其实可以不带cookie
            } catch (SocketTimeoutException | SSLException | SocketException ignored) {
            } catch (Exception e) {
                return null;
            }
        }
        if (res == null) {
            throw new Exception("内容获取异常，跳过");
        }
        String content = res.parse().select("#meta-preload-data").first().attr("content");
        return JSON.parseObject(content).getJSONObject("illust").getJSONObject(String.valueOf(img.getPid())).getJSONObject("urls")
                .getString(quality);
    }

    public static void main(String[] args) throws IOException {
        String proxyhost = "127.0.0.1";
        String proxyport = "10809";
        System.setProperty("proxyHost", proxyhost);
        System.setProperty("proxyPort", proxyport);

        Connection.Response response = Jsoup.connect("https://i.pximg.net/img-original/img/2021/01/23/00/00/04/87229432_p0.jpg").ignoreContentType(true).maxBodySize(1073741824)
                .referrer("https://www.pixiv.net/artworks/" + 87229432).execute();
        System.out.println(Base64.encodeBase64String(response.bodyAsBytes()));
    }

    /**
     * 根据指定大小压缩图片
     *
     * @param imageBytes  源图片字节数组
     * @param desFileSize 指定图片大小，单位kb
     * @return 压缩质量后的图片字节数组
     */

    public static byte[] compressPicForScale(byte[] imageBytes, long desFileSize) throws IOException {
        if (imageBytes == null || imageBytes.length <= 0 || imageBytes.length < desFileSize * 1024) {
            return null;

        }

        long srcSize = imageBytes.length;

        double accuracy = getAccuracy(srcSize / 1024);

        while (imageBytes.length > desFileSize * 1024) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(imageBytes.length);

            Thumbnails.of(inputStream)

                    .scale(accuracy)

                    .outputQuality(accuracy)

                    .toOutputStream(outputStream);

            imageBytes = outputStream.toByteArray();
        }
        return imageBytes;
    }

    /**
     * 自动调节精度(经验数值)
     *
     * @param size 源图片大小
     * @return 图片压缩质量比
     */

    private static double getAccuracy(long size) {
        double accuracy;

        if (size < 900) {
            accuracy = 0.85;

        } else if (size < 2047) {
            accuracy = 0.6;

        } else if (size < 3275) {
            accuracy = 0.44;

        } else {
            accuracy = 0.4;

        }

        return accuracy;

    }
}
