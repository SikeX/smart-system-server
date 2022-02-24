package org.jeecg.modules.smartExportWord.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReport;
import org.jeecg.modules.smartExportWord.util.WordUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @author: lord
 * @date: 2022年02月24日 16:37
 */
@RequestMapping("/postMarriageExport")
@Slf4j
public class PostMarriageExportController {
    @GetMapping(value = "/test")
    public void test01(@RequestParam(name = "ids", required = true) String id, HttpServletResponse response, HttpServletRequest request){

        SmartPostMarriageReport obj = new SmartPostMarriageReport();

        obj.setAge(13);
        obj.setDisposalDescribe("无");
        obj.setGovCarNumber(12);
        obj.setGuestsNumber(12);
        obj.setGuestsScope("同学");
        obj.setIllegalGiftNumber(12);
        obj.setIllegalMoney(20);
        obj.setJob("公务员");
        obj.setJobLevel("大啊的");
        obj.setName("打压");
        obj.setPhoneNumber("16666666666");
        obj.setWorkDepartment("没有");
        obj.setWeddingVenueAddr("你家");
        obj.setWeddingCost(66);
        obj.setSex("男");
        obj.setOtherViolations("无");
        obj.setPoliticsStatus("党员");
        obj.setWeddingVenue("没有");
        obj.setWeddingCarNumber(23);

        SmartPostMarriageReport obj1 = new SmartPostMarriageReport();
        obj1.setAge(13);
        obj1.setDisposalDescribe("无");
        obj1.setGovCarNumber(12);
        obj1.setGuestsNumber(12);
        obj1.setGuestsScope("同学");
        obj1.setIllegalGiftNumber(12);
        obj1.setIllegalMoney(20);
        obj1.setJob("公务员");
        obj1.setJobLevel("大啊的");
        obj1.setName("good");
        obj1.setPhoneNumber("16666666666");
        obj1.setWorkDepartment("没有");
        obj1.setWeddingVenueAddr("你家");
        obj1.setWeddingCost(66);
        obj1.setSex("男");
        obj1.setOtherViolations("无");
        obj1.setPoliticsStatus("党员");
        obj1.setWeddingVenue("没有");
        obj1.setWeddingCarNumber(23);

        //存数据
        List<Map<String, Object>> dataList = new ArrayList<>();
        //生成的word文件名
        List<String> fileNamesList = new ArrayList<>();
        String fileName = obj.getName();
        fileNamesList.add(fileName);
        String fileName1 = obj1.getName();
        fileNamesList.add(fileName1);
        //设置模板
        String ftlTemplateName = "/templates/SmartPostMarriageReport.ftl";

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("obj", obj);
        dataList.add(dataMap);

        Map<String, Object> dataMap1 = new HashMap<>();
        dataMap1.put("obj", obj1);
        dataList.add(dataMap1);

        WordUtils.exportWordBatch(dataList,fileNamesList,ftlTemplateName,response,request);
    }
}
