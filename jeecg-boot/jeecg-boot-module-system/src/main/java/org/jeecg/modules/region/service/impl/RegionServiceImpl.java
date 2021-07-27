package org.jeecg.modules.region.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.modules.region.entity.Region;
import org.jeecg.modules.region.mapper.RegionMapper;
import org.jeecg.modules.region.service.IRegionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @Description: 行政区划
 * @Author: jeecg-boot
 * @Date:   2021-07-05
 * @Version: V1.0
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements IRegionService {

    private static final String[] proviceRegionCode = {"11","12","13","14","15","21","22","23","31","32","33","34",
            "35","36","37","41","42","43","44","45","46","50","51","52","53","54","61","62","63","64","65"};

//    private final String[] proviceRegionCode = {"11","12"};

    private final String gaoUrl = "https://restapi.amap.com/v3/geocode/geo";
    private final String gaoKey = "4138c1cea719cc344b6c876825079684";
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static ExecutorService executor = Executors.newCachedThreadPool();
    private static CountDownLatch cdl = new CountDownLatch(proviceRegionCode.length);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateRegionFillInfo() throws InterruptedException {
        List<Region> updateRegion = new ArrayList<>();
        LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Region::getYear, "2019");
        List<Region> regionList = super.list(wrapper);
        Arrays.stream(proviceRegionCode).forEach(item->
            executor.execute(()-> {
                List<Region> regions = new ArrayList<>();
                List<Region> filterRegionList = regionList.stream().filter(e -> e.getRegionCode().startsWith(item)).collect(Collectors.toList());
                filterRegionList.stream().forEach(region -> regions.add(fillData(region, filterRegionList)));
                logger.info("地区编码{}完善了{}条数据!", item, regions.size());
                updateRegion.addAll(regions);
                cdl.countDown();
            })
        );
        cdl.await();
        System.out.println("123");
        logger.info("共计{}条数据!",updateRegion.size());
        boolean b = updateBatchById(updateRegion);
        logger.info("是否完成数据库更新？{}", b);
        executor.shutdown();
        return null;
    }

    private String[] getItude(String address){
        Map<String,Object> param = new HashMap<>();
        param.put("address",address);
        param.put("key",gaoKey);
        String result = HttpUtil.get(gaoUrl, param, 20000);
        JSONObject jsonObject = JSONUtil.parseObj(result);
        JSONArray geocodes = (JSONArray)jsonObject.get("geocodes");
        String[] str = new String[2];
        if(geocodes != null && geocodes.size() >0){
            String location = ((JSONObject) geocodes.get(0)).get("location").toString();
            str = location.split(StrUtil.COMMA);
        }
        return str;
    }

    private Region fillData(Region region, List<Region> list){
        String parentPath = region.getParentPath();
        List<Region> filterList = list.stream().filter(regionItem->parentPath.contains(regionItem.getRegionCode()))
                .sorted(Comparator.comparing(Region::getRegionType)).collect(Collectors.toList());
        for(Region item:filterList){
            switch (region.getRegionType()){
                case 5:
                    if(4 == item.getRegionType()){
                        region.setTownName(item.getRegionName());
                    }
                case 4:
                    if(3 == item.getRegionType()){
                        region.setCountyName(item.getRegionName());
                    }
                case 3:
                    if(2 == item.getRegionType()){
                        region.setCityName(item.getRegionName());
                    }
                case 2:
                    if(1 == item.getRegionType()){
                        region.setProviceName(item.getRegionName());
                    }
                case 1:
                    break;
            }
        }
        String parentPathName = filterList.stream().map(Region::getRegionName).collect(Collectors.joining(StrUtil.COMMA));
        region.setParentPathName(parentPathName);
        String[] itude = getItude(parentPathName);
        if(itude.length > 0){
            region.setLongitude(itude[0]);
            region.setLatitude(itude[1]);
        }
        return region;
    }
}
