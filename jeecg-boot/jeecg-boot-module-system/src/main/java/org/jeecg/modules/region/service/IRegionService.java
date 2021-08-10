package org.jeecg.modules.region.service;

import org.jeecg.modules.region.entity.Region;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 行政区划
 * @Author: jeecg-boot
 * @Date:   2021-07-05
 * @Version: V1.0
 */
public interface IRegionService extends IService<Region> {

    /**
     * 填充爬取的地区数据信息
     * @return
     */
    String updateRegionFillInfo() throws InterruptedException;

    /**
     * 生成静态页面
     * @return
     */
    String generateHtml();
}
