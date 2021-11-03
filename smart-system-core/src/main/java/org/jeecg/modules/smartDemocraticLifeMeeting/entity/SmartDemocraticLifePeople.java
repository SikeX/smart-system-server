package org.jeecg.modules.smartDemocraticLifeMeeting.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jeecg.common.aspect.annotation.Dict;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 民主生活会参会人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-03
 * @Version: V1.0
 */
@Data
@TableName("smart_democratic_life_people")
@ApiModel(value="smart_democratic_life_people对象", description="民主生活会参会人员表")
public class SmartDemocraticLifePeople implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**主表ID*/
    @ApiModelProperty(value = "主表ID")
    private java.lang.String meetingId;
	/**参会人员工号*/
	@Excel(name = "参会人员工号", width = 15)
    @ApiModelProperty(value = "参会人员工号")
    private java.lang.String participantId;
}
