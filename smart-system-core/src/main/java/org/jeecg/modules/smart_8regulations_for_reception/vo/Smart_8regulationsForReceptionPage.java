package org.jeecg.modules.smart_8regulations_for_reception.vo;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableLogic;
import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptionStaff;
import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptiondStaff;
import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptionActivity;
import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReceptionAppendix;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 八项规定公务接待
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
@Data
@ApiModel(value="smart_8regulations_for_receptionPage对象", description="八项规定公务接待")
public class Smart_8regulationsForReceptionPage {

	/**主键*/
	@ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**单位ID*/
	@Excel(name = "单位ID", width = 15)
	@ApiModelProperty(value = "单位ID")
    private java.lang.String departmentid;
	/**接待时间*/
	@Excel(name = "接待时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "接待时间")
    private java.util.Date receptionTime;
	/**接待对象单位ID*/
	@Excel(name = "接待对象单位ID", width = 15)
	@ApiModelProperty(value = "接待对象单位ID")
    private java.lang.String receptionDepartmentId;
	/**人员数量*/
	@Excel(name = "人员数量", width = 15)
	@ApiModelProperty(value = "人员数量")
    private java.lang.Integer number;
	/**陪同人员数量*/
	@Excel(name = "陪同人员数量", width = 15)
	@ApiModelProperty(value = "陪同人员数量")
    private java.lang.Integer receptionNumber;
	/**总开销金额*/
	@Excel(name = "总开销金额", width = 15)
	@ApiModelProperty(value = "总开销金额")
    private java.lang.Double cost;
	/**创建人id*/
	@Excel(name = "创建人id", width = 15)
	@ApiModelProperty(value = "创建人id")
    private java.lang.String createId;
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
	/**删除状态*/
	@Excel(name = "删除状态", width = 15)
	@TableLogic
	@ApiModelProperty(value = "删除状态")
    private java.lang.Integer delFlag;

	@ExcelCollection(name="八项规定公务接待人员信息")
	@ApiModelProperty(value = "八项规定公务接待人员信息")
	private List<Smart_8regulationsForReceptionStaff> smart_8regulationsForReceptionStaffList;
	@ExcelCollection(name="八项规定公务接待陪同人员信息")
	@ApiModelProperty(value = "八项规定公务接待陪同人员信息")
	private List<Smart_8regulationsForReceptiondStaff> smart_8regulationsForReceptiondStaffList;
	@ExcelCollection(name="八项规定公务接待公务活动项目")
	@ApiModelProperty(value = "八项规定公务接待公务活动项目")
	private List<Smart_8regulationsForReceptionActivity> smart_8regulationsForReceptionActivityList;
	@ExcelCollection(name="八项规定公务接待信息附件表")
	@ApiModelProperty(value = "八项规定公务接待信息附件表")
	private List<Smart_8regulationsForReceptionAppendix> smart_8regulationsForReceptionAppendixList;

}
