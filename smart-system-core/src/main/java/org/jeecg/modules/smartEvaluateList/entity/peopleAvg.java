package org.jeecg.modules.smartEvaluateList.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@ApiModel(value="peopleAvg", description="人员平均分")
@Data
/**
 * @Description: TODO
 * @author: scott
 * @date: 2021年11月18日 12:31
 */
public class peopleAvg {
    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**主管单位*/
    @Excel(name = "主管单位", width = 15)
    @ApiModelProperty(value = "主管单位")
    private java.lang.String exeDept;
    /**政务服务大厅名称*/
    @Excel(name = "政务服务大厅名称", width = 15)
    @ApiModelProperty(value = "政务服务大厅名称")
    private java.lang.String windowsName;
    /**人员名称*/
    @Excel(name = "人员名称", width = 15)
    @ApiModelProperty(value = "人员名称")
    private java.lang.String personName;
    /**评价结果*/
    @Excel(name = "评价结果", width = 15)
    @ApiModelProperty(value = "评价结果")
    @Dict(dicCode = "evaluate_grade")
    private java.lang.String evaluateResult;
    /**
     * 平均分
     */
    private Integer avgGrade;

}
