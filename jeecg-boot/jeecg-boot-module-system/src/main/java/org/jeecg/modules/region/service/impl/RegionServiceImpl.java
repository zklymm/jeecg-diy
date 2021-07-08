package org.jeecg.modules.region.service.impl;

import org.jeecg.modules.region.entity.Region;
import org.jeecg.modules.region.mapper.RegionMapper;
import org.jeecg.modules.region.service.IRegionService;
import org.jeecg.modules.region.utils.GovRegionSpiderPipeline;
import org.jeecg.modules.region.utils.GovRegionSpiderScheduler;
import org.jeecg.modules.region.utils.GovRegionSpiderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import us.codecraft.webmagic.Spider;

/**
 * @Description: 行政区划
 * @Author: jeecg-boot
 * @Date:   2021-07-05
 * @Version: V1.0
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements IRegionService {

    @Autowired
    private GovRegionSpiderUtils govRegionSpiderUtils;
    @Autowired
    private GovRegionSpiderPipeline govRegionSpiderPipeline;

    @Scheduled(cron = "0 54 23 * * ?")
    public void updateRegion() {
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
