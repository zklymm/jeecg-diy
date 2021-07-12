package org.jeecg.modules.region.job;

import org.jeecg.modules.region.utils.GovRegionSpiderPipeline;
import org.jeecg.modules.region.utils.GovRegionSpiderScheduler;
import org.jeecg.modules.region.utils.GovRegionSpiderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

/**
 * @author zk
 * @version 1.0
 * @since 2021-07-09 9:40
 */
@Component
public class RegionJob {

    @Autowired
    private GovRegionSpiderUtils govRegionSpiderUtils;
    @Autowired
    private GovRegionSpiderPipeline govRegionSpiderPipeline;

    @Scheduled(cron = "0 54 23 * * ?")
    public void spiderRegion() {
        System.out.println("爬虫开始执行..........");
        Spider spider = Spider.create(govRegionSpiderUtils)
                //从"https://github.com/code4craft"开始抓
                .addUrl("http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2019/")
//                .addUrl("https://github.com/code4craft")
                //.addUrl("http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2019/45/09/450981.html")
                .addPipeline(govRegionSpiderPipeline)
                //开启5个线程抓取
                .thread(10);
        //启动爬虫
        spider.setExitWhenComplete(true);
        spider.setScheduler(new GovRegionSpiderScheduler("127.0.0.1"));
        spider.run();
    }
}