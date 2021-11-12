package org.jeecg.modules.smart_8regulations_for_reception.entity;

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
 * @Description: 八项规定公务接待人员信息
 * @Author: jeecg-boot
 * @Date:   2021-11-08
 * @Version: V1.0
 */
@ApiModel(value="smart_8regulations_for_reception_staff对象", description="八项规定公务接待人员信息")
@Data
@TableName("smart_8regulations_for_reception_staff")
public class Smart_8regulationsForReceptionStaff implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**人员姓名*/
	@Excel(name = "人员姓名", width = 15)
    @ApiModelProperty(value = "人员姓名")
    private java.lang.String name;
	/**所在单位*/
	@Excel(name = "所在单位", width = 15)
    @ApiModelProperty(value = "所在单位")
    private java.lang.String department;
	/**职务*/
	@Excel(name = "职务", width = 15)
    @ApiModelProperty(value = "职务")
    private java.lang.String jobTitle;
	/**外键*/
    @ApiModelProperty(value = "外键")
    private java.lang.String mainId;
}
