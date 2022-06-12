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
 * @Description: 八项规定公务接待公务活动项目
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
@ApiModel(value="smart_8regulations_for_reception_activity对象", description="八项规定公务接待公务活动项目")
@Data
@TableName("smart_8regulations_for_reception_activity")
public class Smart_8regulationsForReceptionActivity implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**公务活动项目*/
	@Excel(name = "公务活动项目", width = 15)
    @ApiModelProperty(value = "公务活动项目")
    private java.lang.String officialActivitiesProject;
	/**时间*/
	@Excel(name = "时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "时间")
    private java.util.Date time;
	/**场所*/
	@Excel(name = "场所", width = 15)
    @ApiModelProperty(value = "场所")
    private java.lang.String place;
	/**是否安排住宿*/
	@Excel(name = "是否安排住宿", width = 15, dicCode = "stay")
    @ApiModelProperty(value = "是否安排住宿")
    private java.lang.String stay;
	/**花销标准*/
	@Excel(name = "花销标准", width = 15)
    @ApiModelProperty(value = "花销标准")
    private java.lang.String costStandard;
	/**总开销金额*/
	@Excel(name = "总开销金额", width = 15)
    @ApiModelProperty(value = "总开销金额")
    private java.lang.String cost;
	/**创建人工号*/
	@Excel(name = "创建人工号", width = 15)
    @ApiModelProperty(value = "创建人工号")
    private java.lang.String createId;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
    private java.lang.String createTime;
	/**外键*/
    @ApiModelProperty(value = "外键")
    private java.lang.String mainId;
}
