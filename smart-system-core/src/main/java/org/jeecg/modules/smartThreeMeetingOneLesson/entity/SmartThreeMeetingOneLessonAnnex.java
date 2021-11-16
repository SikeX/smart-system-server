package org.jeecg.modules.smartThreeMeetingOneLesson.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 三会一课附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-14
 * @Version: V1.0
 */
@ApiModel(value="smart_three_meeting_one_lesson_annex对象", description="三会一课附件表")
@Data
@TableName("smart_three_meeting_one_lesson_annex")
public class SmartThreeMeetingOneLessonAnnex implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**主表id*/
    @ApiModelProperty(value = "主表id")
    private java.lang.String parentTableId;
	/**序号*/
	@Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "序号")
    private java.lang.Integer serialNumber;
	/**附件说明*/
	@Excel(name = "附件说明", width = 15)
    @ApiModelProperty(value = "附件说明")
    private java.lang.String fileDescription;
	/**附件文件路径*/
	@Excel(name = "附件文件路径", width = 15)
    @ApiModelProperty(value = "附件文件路径")
    private java.lang.String fileLocation;
	/**上传时间*/
	@Excel(name = "上传时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "上传时间")
    private java.util.Date uploadTime;
	/**下载次数*/
	@Excel(name = "下载次数", width = 15)
    @ApiModelProperty(value = "下载次数")
    private java.lang.Integer downloadTimes;
}
