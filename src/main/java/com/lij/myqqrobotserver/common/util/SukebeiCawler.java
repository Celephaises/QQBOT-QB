package com.lij.myqqrobotserver.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lij.myqqrobotserver.common.consts.Const;
import com.lij.myqqrobotserver.entity.Sukebei;

/**
 * @author Celphis
 */
public class SukebeiCawler {

	public static List<Sukebei> getSukebei(String search) {
		String param = search.replaceAll("\\s+", "+");
		Elements sukebeiElements = null;
		try {
			sukebeiElements = Jsoup.connect("https://sukebei.nyaa.si/?f=0&c=0_0&q=" + param + "&s=downloads&o=desc")
					.proxy(Const.pixivProxy).header("Host", "sukebei.nyaa.si").execute().parse()
					.getElementsByTag("tbody").select("tr");
		} catch (IOException e) {
			return null;
		}
		List<Sukebei> sukebeis = new ArrayList<>();
		for (Element element : sukebeiElements) {
			if (sukebeis.size() >= 3) {
				break;
			}
			Elements tds = element.select("td");
			String title = tds.get(1).selectFirst("a").attr("title");
			String megnet = tds.get(2).getElementsByAttributeValueStarting("href", "magnet").get(0).attr("href")
					.replaceAll("&.*", "");
			String size = tds.get(3).text();
			String date = tds.get(4).text();
			sukebeis.add(new Sukebei(title, megnet, size, date));
		}
		return sukebeis;
	}
}
