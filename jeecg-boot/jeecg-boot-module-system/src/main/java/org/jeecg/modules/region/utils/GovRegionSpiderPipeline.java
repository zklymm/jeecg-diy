package org.jeecg.modules.region.utils;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import org.jeecg.modules.region.service.IRegionService;
import org.jeecg.modules.region.entity.Region;

import java.util.List;
import java.util.Map;

/**
 * @author zk
 */
@Component
public class GovRegionSpiderPipeline implements Pipeline {
    @Autowired
    private IRegionService regionService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            List<Region> list = (List<Region>) entry.getValue();
            for(Region region:list){
                LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Region::getRegionCode,region.getRegionCode());
                List<Region> list1 = regionService.list(wrapper);
                if(CollUtil.isNotEmpty(list1)){
                    regionService.update(region,wrapper);
                }else {
                    regionService.save(region);
                }
            }
        }
    }
}
