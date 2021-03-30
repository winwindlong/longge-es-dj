package com.longge.util;

import com.longge.pojo.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HtmlParseUtil {

    public static List<Content> parseJD(String keywords) throws Exception{
        //获取请求
        String url = "https://search.jd.com/Search?keyword=" + keywords;
        //解析网页
        Document document = Jsoup.parse(new URL(url), 30000);
        //所有在js中可以使用的这里都可以用
        Element element = document.getElementById("J_goodsList");
        //获取所有的li元素
        Elements elements = element.getElementsByTag("li");
//        System.out.println(element.html());
        ArrayList<Content> goodsList = new ArrayList<>();
        //获取元素中的内容，这里el就是每一个li
        for (Element element1 : elements) {
            //图片懒加载
            String img = element1.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = element1.getElementsByClass("p-price").eq(0).text();
            String title = element1.getElementsByClass("p-name").eq(0).text();
            String shop = element1.getElementsByClass("curr-shop").eq(0).text();
            Content content = new Content();
            content.setPrice(price);
            content.setShop(shop);
            content.setTitle(title);
            content.setImg(img);
       /*     System.out.println("====================================");
            System.out.println(img);
            System.out.println(price);
            System.out.println(title);
            System.out.println(shop);*/
            goodsList.add(content);

        }
        return goodsList;
    }
    public static void main(String[] args) throws Exception {
        new HtmlParseUtil().parseJD("java").forEach(System.out::println);
    }
}
