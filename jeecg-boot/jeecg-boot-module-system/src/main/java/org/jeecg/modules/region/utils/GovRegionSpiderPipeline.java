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
import java.util.stream.Collectors;

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
            List<String> collect = list.stream().map(Region::getRegionCode).collect(Collectors.toList());
            LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Region::getRegionCode, collect);
            List<Region> oldList = regionService.list(wrapper);
            // 更新数据
            List<Region> updateList = list.stream().filter(item -> oldList.stream().anyMatch(e -> e.getRegionCode().equals(item.getRegionCode()))).collect(Collectors.toList());
            updateList.stream().forEach(item-> oldList.stream().forEach(e->{
                if(e.getRegionCode().equals(item.getRegionCode())){
                    item.setId(e.getId());
                }
            }));
            // 新增数据
            List<Region> insertList = list.stream().filter(item -> updateList.stream().allMatch(e -> !e.getRegionCode().equals(item.getRegionCode()))).collect(Collectors.toList());
            regionService.updateBatchById(updateList);
            regionService.saveBatch(insertList);
        }
    }
}
