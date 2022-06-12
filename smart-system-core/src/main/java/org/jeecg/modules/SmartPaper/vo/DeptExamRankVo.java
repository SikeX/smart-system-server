package org.jeecg.modules.SmartPaper.vo;

import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @description: 单位排名
 * @author zxh
 * @date 2021/12/13 17:03
 * @version 1.0
 */
@Data
public class DeptExamRankVo {
    private String examId;
    private String examName;
    private String deptId;
    private String deptName;
    private Float deptAvgGrade;
    private Integer count;
}
