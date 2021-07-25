package org.jeecg.modules.region.service.impl;

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
import java.util.stream.Collectors;

/**
 * @Description: 行政区划
 * @Author: jeecg-boot
 * @Date:   2021-07-05
 * @Version: V1.0
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements IRegionService {

    private final String[] proviceRegionCode = {"11","12","13","14","15","21","22","23","31","32","33","34",
            "35","36","37","41","42","43","44","45","46","50","51","52","53","54","61","62","63","64","65"};

    private final String gaoUrl = "https://restapi.amap.com/v3/geocode/geo";
    private final String gaoKey = "4138c1cea719cc344b6c876825079684";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateRegionFillInfo() {
        LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
        Arrays.stream(proviceRegionCode).forEach(item->{
            List<Region> regions = new ArrayList<>();
            wrapper.likeRight(Region::getRegionCode, item);
            wrapper.eq(Region::getYear, "2019");
            List<Region> list = super.list(wrapper);
            for(Region region:list){
                String[] parentPathArray = region.getParentPath() == null ?new String[]{}:region.getParentPath().split(StrUtil.COMMA);
                if(parentPathArray.length < 1){
                    logger.warn("error data {}", region);
                    continue;
                }
                List<Region> filterList = list.stream().filter(regionItem ->
                        Arrays.stream(parentPathArray).allMatch(parentItem->parentItem.equals(regionItem.getRegionCode())))
                        .sorted(Comparator.comparing(Region::getRegionType)).collect(Collectors.toList());
                List<Region> proviceList = filterList.stream().filter(regionItem -> 1 == regionItem.getRegionType()).collect(Collectors.toList());
                List<Region> cityList = filterList.stream().filter(regionItem -> 2 == regionItem.getRegionType()).collect(Collectors.toList());
                List<Region> countyList = filterList.stream().filter(regionItem -> 3 == regionItem.getRegionType()).collect(Collectors.toList());
                List<Region> townList = filterList.stream().filter(regionItem -> 4 == regionItem.getRegionType()).collect(Collectors.toList());
                String parentPathName = filterList.stream().map(Region::getRegionName).collect(Collectors.joining(StrUtil.COMMA));
                region.setParentPathName(parentPathName);
                switch (region.getRegionType()){
                    case 5:
                        region.setTownName(townList.stream().findFirst().orElse(new Region()).getRegionName());
                    case 4:
                        region.setCountyName(countyList.stream().findFirst().orElse(new Region()).getRegionName());
                    case 3:
                        region.setCityName(cityList.stream().findFirst().orElse(new Region()).getRegionName());
                    case 2:
                        region.setProviceName(proviceList.stream().findFirst().orElse(new Region()).getRegionName());
                    case 1:
                        break;
                }
                if(region.getRegionType() < 4){
                    String[] itude = getItude(parentPathName);
                    if(itude.length > 0){
                        region.setLongitude(itude[0]);
                        region.setLatitude(itude[1]);
                    }
                }
                regions.add(region);
            }
            logger.info("完善了{}条数据!",regions.size());
            boolean b = updateBatchById(regions);
            logger.info("是否完成数据库更新？{}", b);
        });
        return null;
    }

    private String[] getItude(String address){
        Map<String,Object> param = new HashMap<>();
        param.put("address",address);
        param.put("key",gaoKey);
        String result = HttpUtil.get(gaoUrl, param);
        JSONObject jsonObject = JSONUtil.parseObj(result);
        JSONArray geocodes = (JSONArray)jsonObject.get("geocodes");
        String[] str = new String[2];
        if(geocodes != null && geocodes.size() >0){
            String location = ((JSONObject) geocodes.get(0)).get("location").toString();
            str = location.split(StrUtil.COMMA);
        }
        return str;
    }
}
