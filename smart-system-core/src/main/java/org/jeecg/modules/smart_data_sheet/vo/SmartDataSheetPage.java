package org.jeecg.modules.smart_data_sheet.vo;

import java.util.List;
import org.jeecg.modules.smart_data_sheet.entity.SmartDataSheet;
import org.jeecg.modules.smart_data_sheet.entity.SmartDataSheetFile;
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
 * @Description: 资料库
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
@Data
@ApiModel(value="smart_data_sheetPage对象", description="资料库")
public class SmartDataSheetPage {

	/**主键*/
	@ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**文件名*/
	@Excel(name = "文件名", width = 15)
	@ApiModelProperty(value = "文件名")
    private java.lang.String name;
	/**文件类型*/
	@Excel(name = "文件类型", width = 15, dicCode = "type_data")
    @Dict(dicCode = "type_data")
	@ApiModelProperty(value = "文件类型")
    private java.lang.Integer type;
	/**发布时间*/
	@Excel(name = "发布时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "发布时间")
    private java.util.Date time;
	/**发布人*/
	@Excel(name = "发布人", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
	@ApiModelProperty(value = "发布人")
    private java.lang.String publisher;
	/**文件描述*/
	@Excel(name = "文件描述", width = 15)
	@ApiModelProperty(value = "文件描述")
    private java.lang.String describe_;
	/**删除状态*/
	@Excel(name = "删除状态", width = 15)
	@ApiModelProperty(value = "删除状态")
    private java.lang.Integer delFlag;
	/**单位id*/
	@Excel(name = "单位id", width = 15)
	@ApiModelProperty(value = "单位id")
    private java.lang.String departmentid;

	@ExcelCollection(name="资料库文件")
	@ApiModelProperty(value = "资料库文件")
	private List<SmartDataSheetFile> smartDataSheetFileList;

	private Date createTime;

}
