package org.jeecg.modules.smartAnswerRank.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 排名
 * @Author: jeecg-boot
 * @Date:   2022-02-21
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Rank implements Serializable {
    private static final long serialVersionUID = 1L;


	/**单位*/
	@Excel(name = "单位", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
	@Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @ApiModelProperty(value = "单位")
    private String departId;

    @ApiModelProperty(value = "单位名称")
    private String departName;


    private Map<String, Double> scoreMap;

    private Map<String, Double> columnMap;


    /**总分*/
    @Excel(name = "总分", width = 15)
    @ApiModelProperty(value = "总分")
    private Double totalScore;
	/**排名*/
	@Excel(name = "排名", width = 15)
    @ApiModelProperty(value = "排名")
    private Integer rank;
    /**总分*/
    @Excel(name = "去年排名", width = 15)
    @ApiModelProperty(value = "去年排名")
    private Integer lastRank;
}
