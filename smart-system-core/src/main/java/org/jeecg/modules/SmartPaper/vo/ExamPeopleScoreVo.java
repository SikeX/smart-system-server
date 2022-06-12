package org.jeecg.modules.SmartPaper.vo;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description:
 * @Author: sike
 * @Date: 2021/12/9 1:58
 * @Version: V1.0
 */
@Data
public class ExamPeopleScoreVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String examId;
    private String examName;
    private String deptId;
    @Excel(name="单位",width=15)
    private String deptName;
    @Excel(name="姓名",width=15)
    private String personName;
    @Excel(name="成绩(调查结果)",width=15)
    private String examGrade;
    private Integer isMark;
}
