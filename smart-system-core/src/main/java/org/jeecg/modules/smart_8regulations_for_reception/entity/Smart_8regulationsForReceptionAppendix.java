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
 * @Description: 八项规定公务接待信息附件表
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
@ApiModel(value="smart_8regulations_for_reception_appendix对象", description="八项规定公务接待信息附件表")
@Data
@TableName("smart_8regulations_for_reception_appendix")
public class Smart_8regulationsForReceptionAppendix implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**序号*/
	@Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "序号")
    private java.lang.String number;
	/**附件说明*/
	@Excel(name = "附件说明", width = 15)
    @ApiModelProperty(value = "附件说明")
    private java.lang.String attachmentDescription;
	/**附件文件路径*/
	@Excel(name = "附件文件路径", width = 15)
    @ApiModelProperty(value = "附件文件路径")
    private java.lang.String path;
	/**上传时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "上传时间")
    private java.util.Date updateTime;
	/**下载次数*/
	@Excel(name = "下载次数", width = 15)
    @ApiModelProperty(value = "下载次数")
    private java.lang.Integer downloadTimes;
	/**外键*/
    @ApiModelProperty(value = "外键")
    private java.lang.String mianId;
}
