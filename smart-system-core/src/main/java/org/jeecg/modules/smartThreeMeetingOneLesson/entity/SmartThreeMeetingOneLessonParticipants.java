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
 * @Description: 三会一课参会人员表
 * @Author: jeecg-boot
 * @Date:   2021-11-14
 * @Version: V1.0
 */
@ApiModel(value="smart_three_meeting_one_lesson_participants对象", description="三会一课参会人员表")
@Data
@TableName("smart_three_meeting_one_lesson_participants")
public class SmartThreeMeetingOneLessonParticipants implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**主表id*/
    @ApiModelProperty(value = "主表id")
    private java.lang.String parentTableId;
	/**参会人员工号*/
	@Excel(name = "参会人员工号", width = 15)
    @ApiModelProperty(value = "参会人员工号")
    private java.lang.String participantsNumber;
}
