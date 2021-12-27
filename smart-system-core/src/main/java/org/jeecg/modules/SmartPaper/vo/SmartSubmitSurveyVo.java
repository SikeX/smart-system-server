package org.jeecg.modules.SmartPaper.vo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.SmartPaper.entity.SmartSubmit;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor //全参构造函数
@NoArgsConstructor  //无参构造函数
public class SmartSubmitSurveyVo {
    /**主键*/
    @ApiModelProperty(value = "主键")
    private String id;
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
    /**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**更新日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
    /**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    /**人员id*/
    @ApiModelProperty(value = "人员id")
    private String personId;
    /**调查人员id*/
    @ApiModelProperty(value = "调查人员id")
    private String inquirerId;
    /**姓名*/
    @Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
    private String personName;
    /**考试id*/
    @Excel(name = "考试id", width = 15)
    @ApiModelProperty(value = "考试id")
    private String examId;
    /**是否评分*/
    private Integer isMark;
    /**成绩*/
    @Excel(name = "成绩", width = 15)
    @ApiModelProperty(value = "成绩")
    private Integer examGrade;
    /**提交时间*/
    @Excel(name = "提交时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "提交时间")
    private Date submitTime;
    /**ip地址*/
    @Excel(name = "ip地址", width = 15)
    @ApiModelProperty(value = "ip地址")
    private String ipAddress;

    List<SmartSubmit> smartSubmitList;
    /**满意度*/
    @Dict(dicCode = "evaluate_grade")
    private String satisfaction;
    /**是否完成调查*/
    private String isFinish;
    /**是否发现线索*/
    private String isReport;
}
