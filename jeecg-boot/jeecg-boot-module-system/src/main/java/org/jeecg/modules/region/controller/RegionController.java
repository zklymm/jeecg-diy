package org.jeecg.modules.region.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.region.entity.Region;
import org.jeecg.modules.region.service.IRegionService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 行政区划
 * @Author: jeecg-boot
 * @Date:   2021-07-05
 * @Version: V1.0
 */
@Api(tags="行政区划")
@RestController
@RequestMapping("/region/region")
@Slf4j
public class RegionController extends JeecgController<Region, IRegionService> {
	@Autowired
	private IRegionService regionService;
	
	/**
	 * 分页列表查询
	 *
	 * @param region
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "行政区划-分页列表查询")
	@ApiOperation(value="行政区划-分页列表查询", notes="行政区划-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Region region,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Region> queryWrapper = QueryGenerator.initQueryWrapper(region, req.getParameterMap());
		Page<Region> page = new Page<Region>(pageNo, pageSize);
		IPage<Region> pageList = regionService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param region
	 * @return
	 */
	@AutoLog(value = "行政区划-添加")
	@ApiOperation(value="行政区划-添加", notes="行政区划-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Region region) {
		regionService.save(region);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param region
	 * @return
	 */
	@AutoLog(value = "行政区划-编辑")
	@ApiOperation(value="行政区划-编辑", notes="行政区划-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Region region) {
		regionService.updateById(region);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "行政区划-通过id删除")
	@ApiOperation(value="行政区划-通过id删除", notes="行政区划-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		regionService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "行政区划-批量删除")
	@ApiOperation(value="行政区划-批量删除", notes="行政区划-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.regionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "行政区划-通过id查询")
	@ApiOperation(value="行政区划-通过id查询", notes="行政区划-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Region region = regionService.getById(id);
		if(region==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(region);
	}

//	 /**
//	  * test
//	  *
//	  * @return
//	  */
//	 @AutoLog(value = "行政区划-测试")
//	 @ApiOperation(value="行政区划-测试", notes="行政区划-测试")
//	 @GetMapping(value = "/test")
//	 public Result<?> test() {
//		 regionService.updateRegionFillInfo();
//		 return Result.OK();
//	 }


	 /**
    * 导出excel
    *
    * @param request
    * @param region
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Region region) {
        return super.exportXls(request, region, Region.class, "行政区划");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Region.class);
    }

}
