package org.jeecg.modules.region.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 行政区划
 * @Author: jeecg-boot
 * @Date:   2021-07-05
 * @Version: V1.0
 */
@Data
@TableName("tb_region")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tb_region对象", description="行政区划")
public class Region implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**行政区编号*/
	@Excel(name = "行政区编号", width = 15)
    @ApiModelProperty(value = "行政区编号")
    private java.lang.String regionCode;
	/**行政区名称*/
	@Excel(name = "行政区名称", width = 15)
    @ApiModelProperty(value = "行政区名称")
    private java.lang.String regionName;
	/**类型1.省级 2.市级 3.县区 4.镇 5.村*/
	@Excel(name = "类型1.省级 2.市级 3.县区 4.镇 5.村", width = 15)
    @ApiModelProperty(value = "类型1.省级 2.市级 3.县区 4.镇 5.村")
    private java.lang.Integer regionType;
	/**城乡分类标识111主城区 112城乡结合区 121镇中心区 122镇乡结合区 123特殊区域 210乡中心区 220村庄*/
	@Excel(name = "城乡分类标识111主城区 112城乡结合区 121镇中心区 122镇乡结合区 123特殊区域 210乡中心区 220村庄", width = 15)
    @ApiModelProperty(value = "城乡分类标识111主城区 112城乡结合区 121镇中心区 122镇乡结合区 123特殊区域 210乡中心区 220村庄")
    private java.lang.String countyType;
	/**上级code*/
	@Excel(name = "上级code", width = 15)
    @ApiModelProperty(value = "上级code")
    private java.lang.String parentCode;
	/**全部上级*/
	@Excel(name = "全部上级", width = 15)
    @ApiModelProperty(value = "全部上级")
    private java.lang.String parentPath;
	/**全部上级名称*/
	@Excel(name = "全部上级名称", width = 15)
    @ApiModelProperty(value = "全部上级名称")
    private java.lang.String parentPathName;
	/**省级名称*/
	@Excel(name = "省级名称", width = 15)
    @ApiModelProperty(value = "省级名称")
    private java.lang.String proviceName;
	/**市级名称*/
	@Excel(name = "市级名称", width = 15)
    @ApiModelProperty(value = "市级名称")
    private java.lang.String cityName;
	/**区县级名称*/
	@Excel(name = "区县级名称", width = 15)
    @ApiModelProperty(value = "区县级名称")
    private java.lang.String countyName;
	/**乡镇级名称*/
	@Excel(name = "乡镇级名称", width = 15)
    @ApiModelProperty(value = "乡镇级名称")
    private java.lang.String townName;
	/**村级名称*/
	@Excel(name = "村级名称", width = 15)
    @ApiModelProperty(value = "村级名称")
    private java.lang.String villageName;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String longitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String latitude;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private java.lang.String description;
	/**年份*/
    @Excel(name = "年份", width = 15)
    @ApiModelProperty(value = "年份")
    private java.lang.String year;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
}
