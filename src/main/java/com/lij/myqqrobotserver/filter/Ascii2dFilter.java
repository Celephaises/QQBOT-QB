package com.lij.myqqrobotserver.filter;

import com.lij.myqqrobotserver.dto.MessageDto;
import com.lij.myqqrobotserver.filter.base.BaseFilter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * @author Celphis
 * <p>搜图过滤器</p>
 */
@Component
public class Ascii2dFilter extends BaseFilter {
	
	
   
	private static final CopyOnWriteArraySet<String> QQ_NUM_SET = new CopyOnWriteArraySet<>();
   
    @Override
    protected MessageDto checkMessage(MessageDto inDto) throws IOException {
        String rawMessage = inDto.rawMessage;
        String regaxOrder = "^\\s*以图(搜|识)图\\s*$";
        String regaxImg = "^\\[CQ:image,\\S+\\]$";
        if (!rawMessage.matches(regaxOrder) && !rawMessage.matches(regaxImg)) {
            return null;
        }
        if (rawMessage.matches(regaxOrder)) {
            QQ_NUM_SET.add(inDto.fromQQ);
            return parseMessage(inDto, "请发送想要搜索的图片");
        }
        if (QQ_NUM_SET.contains(inDto.fromQQ) && rawMessage.matches(regaxImg)) {
            QQ_NUM_SET.remove(inDto.fromQQ);
            String imgUrl = inDto.message.replaceAll("\\S*url=", "").replaceAll("\\]", "");
            Document document = null;
            try {
                document = Jsoup.connect("https://ascii2d.net/search/uri").method(Connection.Method.POST).data("uri", imgUrl).execute().parse();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String hash = document.getElementsByClass("hash").first().text();
            List<RowItem> items = new ArrayList<>();
            Elements rows = Jsoup.connect("https://ascii2d.net/search/bovw/" + hash).method(Connection.Method.GET).execute().parse().getElementsByClass("row item-box").select("div:has(div:has(div:has(h6)))");
            for (Element row : rows) {
                String itemName = row.select("a[href*=http]").first().text();
                String itemUrl = row.select("a[href*=http]").first().attr("href");
                String author;
                try {
                    author = row.select("a[href*=http]").first().nextElementSibling().text();
                } catch (Exception e) {
                    author = null;
                }
                String itemChannel;
                try {
                    itemChannel = row.select("h6:has(small)").select("small").first().text();
                } catch (Exception e) {
                    itemChannel = null;
                }
                String itemPreivew = "https://ascii2d.net" + row.select("img[src^=/thumbnail]").first().attr("src");
                items.add(new RowItem(itemName, itemUrl, itemPreivew, itemChannel, author));
                if (items.size() >= 3) {
                    break;
                }
            }
            StringBuilder msg = new StringBuilder();
            List<RowItem> distinctList = items.stream().distinct().collect(Collectors.toList());
            for (RowItem item : distinctList) {
                msg.append("作品名称: ").append(item.itemName)
                        .append("\n作者名称: ").append(item.author)
                        .append("\n来源: ").append(item.itemChannel)
                        .append("\n链接地址: ").append(item.itemUrl)
                        .append("[CQ:image,file=").append(item.itemPreviewUrl).append("]\n");
            }
            return parseMessage(inDto, msg.toString());
        }
        return null;
    }

    static class RowItem {

        public String itemName;
        public String itemUrl;
        public String itemPreviewUrl;
        public String itemChannel;
        public String author;

        public RowItem(String itemName, String itemUrl, String itemPreviewUrl, String itemChannel, String author) {
            this.itemName = itemName;
            this.itemUrl = itemUrl;
            this.itemPreviewUrl = itemPreviewUrl;
            this.itemChannel = itemChannel;
            this.author = author;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RowItem rowItem = (RowItem) o;
            return itemName.equals(rowItem.itemName) && itemUrl.equals(rowItem.itemUrl) && Objects.equals(author, rowItem.author);
        }

        @Override
        public int hashCode() {
            return Objects.hash(itemName, itemUrl, author);
        }

        @Override
        public String toString() {
            return "RowItem{" +
                    "itemName='" + itemName + '\'' +
                    ", itemUrl='" + itemUrl + '\'' +
                    ", itemPreviewUrl='" + itemPreviewUrl + '\'' +
                    ", itemChannel='" + itemChannel + '\'' +
                    ", author='" + author + '\'' +
                    '}';
        }
    }

	@Override
	protected void init() {
		commendStr="以图搜图|以图识图";
		
	}
}
