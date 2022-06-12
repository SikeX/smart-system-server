package org.jeecg.modules.SmartPaper.vo;

import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @author zxh
 * @version 1.0
 * @description: TODO
 * @date 2021/12/24 20:11
 */
@Data
public class RandomPeople implements Serializable {
    @Dict(dictTable = "smart_paper",dicCode = "id",dicText = "paper_name")
    private String paperId;
    private String paperName;
    private String paperType;
    /**户主*/
//    @Excel(name="户主",width = 15,dictTable = "sys_user",dicCode = "idnumber",dicText = "realname")
//    @Dict(dictTable = "sys_user",dicCode = "idnumber",dicText = "realname")
    private String idnumber;
    @Excel(name="户主")
    private String hostName;
    /**被访人信息*/
//    @Excel(name="被访人",width = 15,dictTable = "sys_user",dicCode = "id",dicText = "realname")
//    @Dict(dictTable = "sys_user",dicCode = "id",dicText = "realname")
    private String userId;
    @Excel(name="被访人")
    private String realname;
    private String phone;
    @Dict(dictTable = "sys_depart",dicCode = "id",dicText = "depart_name")
    private String departId;
    private String departName;
    /**满意度*/
    //@Dict(dicCode = "evaluate_grade")
    //@Excel(name = "满意度",width = 15,dicCode = "evaluate_grade")
    //private String satisfaction;
    /**是否完成调查*/
    @Excel(name = "是否完成调查",width = 15,dicCode = "is_finish")
    @Dict(dicCode = "is_finish")
    private String isFinish;
    /**是否汇报问题*/
    @Excel(name = "是否汇报问题",width = 15,dicCode = "yn")
    @Dict(dicCode = "yn")
    private String isReport;
    private String isMark;
}
