package org.jeecg.modules.region.utils;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import org.jeecg.modules.region.entity.Region;

import java.util.*;

@Component
public class GovRegionSpiderUtils implements PageProcessor {
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setCharset("gbk").setRetryTimes(10).setSleepTime(10000).setTimeOut(60000);

    private String year = "2019";

//    private String gaoUrl = "https://restapi.amap.com/v3/geocode/geo";
//    private String gaoKey = "4138c1cea719cc344b6c876825079684";
    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        // 部分二：定义如何抽取页面信息，并保存下来
//        page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
        List<String> provNameList = page.getHtml().xpath("//tr[@class='provincetr']/td/a/text()").all();
        List<String> provCodeList = page.getHtml().xpath("//tr[@class='provincetr']/td/a/@href").all();
        List<String> cityNameList = page.getHtml().xpath("//tr[@class='citytr']/td[2]/a/text()").all();
        List<String> cityCodeList = page.getHtml().xpath("//tr[@class='citytr']/td[1]/a/text()").all();
        List<String> countyNameList = page.getHtml().xpath("//tr[@class='countytr']/td[2]/a/text()").all();
        List<String> countyCodeList = page.getHtml().xpath("//tr[@class='countytr']/td[1]/a/text()").all();
        List<String> townNameList = page.getHtml().xpath("//tr[@class='towntr']/td[2]/a/text()").all();
        List<String> townCodeList = page.getHtml().xpath("//tr[@class='towntr']/td[1]/a/text()").all();
        List<String> villageNameList = page.getHtml().xpath("//tr[@class='villagetr']/td[3]/text()").all();
        List<String> villageTypeList = page.getHtml().xpath("//tr[@class='villagetr']/td[2]/text()").all();
        List<String> villageCodeList = page.getHtml().xpath("//tr[@class='villagetr']/td[1]/text()").all();
        List<Region> list = new ArrayList<>();
        if(provNameList.size()>0 && provCodeList.size()>0){
            if(provNameList.size() != provCodeList.size()){
                return;
            }
            for(int i=0;i<provNameList.size();i++){
                Region entity = new Region();
                entity.setRegionCode(provCodeList.get(i).substring(0,provCodeList.get(i).indexOf("."))+"0000000000");
                entity.setRegionName(provNameList.get(i));
                entity.setRegionType(1);
                entity.setProviceName(provNameList.get(i));
                entity.setYear(year);
                entity.setParentCode("0");
                entity.setParentPath(provCodeList.get(i).substring(0,provCodeList.get(i).indexOf("."))+"0000000000");
                list.add(entity);
            }
        }else if(cityNameList.size()>0 && cityCodeList.size()>0){
            if(cityNameList.size() != cityCodeList.size()){
                return;
            }
            for(int i=0;i<cityNameList.size();i++){
                String proviceCode = cityCodeList.get(i).substring(0,2)+"0000000000";
                Region entity = new Region();
                entity.setRegionCode(cityCodeList.get(i));
                entity.setRegionName(cityNameList.get(i));
                entity.setParentCode(proviceCode);
                entity.setRegionType(2);
                entity.setCityName(cityNameList.get(i));
                entity.setParentPath(proviceCode+","+cityCodeList.get(i));
                entity.setYear(year);
                list.add(entity);
            }
        }else if(countyNameList.size()>0 && countyCodeList.size()>0){
            if(countyNameList.size() != countyCodeList.size()){
                return;
            }
            for(int i=0;i<countyNameList.size();i++){
                String proviceCode = countyCodeList.get(i).substring(0,2)+"0000000000";
                String cityCode = countyCodeList.get(i).substring(0,4)+"00000000";
                Region entity = new Region();
                entity.setRegionCode(countyCodeList.get(i));
                entity.setRegionName(countyNameList.get(i));
                entity.setRegionType(3);
                entity.setParentCode(cityCode);
                entity.setYear(year);
                entity.setCountyName(countyNameList.get(i));
                entity.setParentPath(proviceCode+","+cityCode+","+countyCodeList.get(i));
                list.add(entity);
            }
        }else if(townNameList.size()>0 && townCodeList.size()>0){
            if(townNameList.size() != townCodeList.size()){
                return;
            }
            for(int i=0;i<townNameList.size();i++){
                String proviceCode = townCodeList.get(i).substring(0,2)+"0000000000";
                String cityCode = townCodeList.get(i).substring(0,4)+"00000000";
                String countyCode = townCodeList.get(i).substring(0,6)+"000000";
                Region entity = new Region();
                entity.setRegionCode(townCodeList.get(i));
                entity.setRegionName(townNameList.get(i));
                entity.setRegionType(4);
                entity.setParentCode(countyCode);
                entity.setYear(year);
                entity.setTownName(townNameList.get(i));
                entity.setParentPath(proviceCode+","+cityCode+","+countyCode+","+townCodeList.get(i));
                list.add(entity);
            }
        }else if(villageNameList.size()>0 && villageTypeList.size()>0 && villageCodeList.size()>0){
            if(villageNameList.size() != villageTypeList.size() || villageTypeList.size() != villageCodeList.size()){
                return;
            }
            for(int i=0;i<villageNameList.size();i++){
                String proviceCode = villageCodeList.get(i).substring(0,2)+"0000000000";
                String cityCode = villageCodeList.get(i).substring(0,4)+"00000000";
                String countyCode = villageCodeList.get(i).substring(0,6)+"000000";
                String townCode = villageCodeList.get(i).substring(0,9)+"000";
                Region entity = new Region();
                entity.setRegionCode(villageCodeList.get(i));
                entity.setRegionName(villageNameList.get(i));
                entity.setCountyType(villageTypeList.get(i));
                entity.setRegionType(5);
                entity.setParentCode(townCode);
                entity.setYear(year);
                entity.setVillageName(villageNameList.get(i));
                entity.setParentPath(proviceCode+","+cityCode+","+countyCode+","+townCode+","+villageCodeList.get(i));
                list.add(entity);
            }
        }
        page.putField("list",list);
        // 部分三：从页面发现后续的url地址来抓取
        page.addTargetRequests(page.getHtml().xpath("//tr[@class='provincetr']/td/a/@href|//tr[@class='citytr']/td[1]/a/@href|//tr[@class='countytr']/td[1]/a/@href|//tr[@class='towntr']/td[1]/a/@href").all());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
