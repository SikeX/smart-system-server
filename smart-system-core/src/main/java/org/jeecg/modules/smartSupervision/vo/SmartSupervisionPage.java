package org.jeecg.modules.smartSupervision.vo;

import java.util.List;
import org.jeecg.modules.smartSupervision.entity.SmartSupervision;
import org.jeecg.modules.smartSupervision.entity.SmartSupervisionAnnex;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 八项规定监督检查表
 * @Author: jeecg-boot
 * @Date:   2021-11-08
 * @Version: V1.0
 */
@Data
@ApiModel(value="smart_supervisionPage对象", description="八项规定监督检查表")
public class SmartSupervisionPage {

	/**主键*/
	@ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
	@ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
	@ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
	@ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**标题*/
	@Excel(name = "标题", width = 15)
	@ApiModelProperty(value = "标题")
    private java.lang.String title;
	/**正文*/
	@Excel(name = "正文", width = 15)
	@ApiModelProperty(value = "正文")
    private java.lang.String content;
	/**监督检查时间*/
	@Excel(name = "监督检查时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "监督检查时间")
    private java.util.Date supervisionTime;
	/**创建人工号*/
	@Excel(name = "创建人工号", width = 15)
	@ApiModelProperty(value = "创建人工号")
    private java.lang.String creatorNo;

	@ExcelCollection(name="8项规定监督检查附件表")
	@ApiModelProperty(value = "8项规定监督检查附件表")
	private List<SmartSupervisionAnnex> smartSupervisionAnnexList;

}
