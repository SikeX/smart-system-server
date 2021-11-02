package org.jeecg.modules.smartInnerParty.entity;

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
 * @Description: 党内谈话参与人表
 * @Author: jeecg-boot
 * @Date:   2021-11-02
 * @Version: V1.0
 */
@ApiModel(value="smart_inner_party_pacpa对象", description="党内谈话参与人表")
@Data
@TableName("smart_inner_party_pacpa")
public class SmartInnerPartyPacpa implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**党内谈话表ID*/
    @ApiModelProperty(value = "党内谈话表ID")
    private java.lang.String innerPartyId;
	/**参会人员工号*/
	@Excel(name = "参会人员工号", width = 15)
    @ApiModelProperty(value = "参会人员工号")
    private java.lang.String participantNo;
}
