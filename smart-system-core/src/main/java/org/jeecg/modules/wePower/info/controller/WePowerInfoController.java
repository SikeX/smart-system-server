package org.jeecg.modules.wePower.info.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.VillageInfo;
import org.jeecg.modules.wePower.info.constant.VillagePeopleTypeConstant;
import org.jeecg.modules.wePower.info.vo.People;
import org.jeecg.modules.wePower.info.vo.WePowerInfoVo;
import org.jeecg.modules.wePower.smartGroupEconomy.entity.SmartGroupEconomy;
import org.jeecg.modules.wePower.smartGroupEconomy.entity.SmartGroupEconomyPeople;
import org.jeecg.modules.wePower.smartGroupEconomy.service.ISmartGroupEconomyPeopleService;
import org.jeecg.modules.wePower.smartVillageLead.entity.SmartVillageLead;
import org.jeecg.modules.wePower.smartVillageLead.service.ISmartVillageLeadService;
import org.jeecg.modules.wePower.smartVillageLead2.entity.SmartVillageLead2;
import org.jeecg.modules.wePower.smartVillageLead2.service.ISmartVillageLead2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: sike
 * @Date: 2022/3/31 17:39
 * @Version: V1.0
 */
@Api(tags = "信息公示")
@RestController
@RequestMapping("/wePowerInfo")
@Slf4j
public class WePowerInfoController {

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Autowired
    private ISmartVillageLead2Service smartVillageLead2Service;

    @Autowired
    private ISmartVillageLeadService smartVillageLeadService;

    @Autowired
    private ISmartGroupEconomyPeopleService smartGroupEconomyPeopleService;

    @GetMapping(value = "/getInfo")
    public Result<?> getInfo(@RequestParam(name = "locationId") String locationId) {
        WePowerInfoVo wePowerInfoVo = new WePowerInfoVo();
        VillageInfo villageInfo = sysBaseAPI.getVillageInfoByDepartId(locationId);
        wePowerInfoVo.setLocationName(villageInfo.getLocationName());
        wePowerInfoVo.setHomeNumber(villageInfo.getHomeNumber());
        wePowerInfoVo.setPopulation(villageInfo.getPopulation());

        List<People> dangZhiBuList = new ArrayList<>();
        List<People> committeeList = new ArrayList<>();

        QueryWrapper<SmartVillageLead> leadQueryWrapper1 = new QueryWrapper<>();
        leadQueryWrapper1.eq("location", locationId);
        smartVillageLeadService.list(leadQueryWrapper1).forEach(smartVillageLead -> {
            if(smartVillageLead == null) {
                log.info("smartVillageLead is null");
                return;
            }
            People people = new People();
            people.setName(smartVillageLead.getName());
            people.setImg(smartVillageLead.getPicture());
            if (smartVillageLead.getPeopleType().equals(VillagePeopleTypeConstant.dangZhibu)) {
                String jobName = sysBaseAPI.translateDict("lead_job", smartVillageLead.getJob());
                people.setDangZhiBuJob(jobName);
                dangZhiBuList.add(people);
            } else if (smartVillageLead.getPeopleType().equals(VillagePeopleTypeConstant.committee)) {
                people.setDangZhiBuJob(smartVillageLead.getJob());
                committeeList.add(people);
            }
        });
        wePowerInfoVo.setDangZhiBuList(dangZhiBuList);
        wePowerInfoVo.setCommitteeList(committeeList);


        List<People> dangYuanList = new ArrayList<>();
        List<People> villageRepresentativeList = new ArrayList<>();

        QueryWrapper<SmartVillageLead2> leadQueryWrapper2 = new QueryWrapper<>();
        leadQueryWrapper2.eq("village", locationId);
        smartVillageLead2Service.list(leadQueryWrapper2).forEach(smartVillageLead2 -> {
            if(smartVillageLead2 == null) {
                log.info("smartVillageLead2 is null");
                return;
            }
            People people = new People();
            people.setName(smartVillageLead2.getName());
            people.setImg(smartVillageLead2.getPic());
            if(smartVillageLead2.getType().equals(VillagePeopleTypeConstant.dangYuan)){
                dangYuanList.add(people);
            } else if (smartVillageLead2.getType().equals(VillagePeopleTypeConstant.villageRepresentative)) {
                villageRepresentativeList.add(people);
            }
        });

        wePowerInfoVo.setDangYuanList(dangYuanList);
        wePowerInfoVo.setVillageRepresentativeList(villageRepresentativeList);


        List<People> councilList = new ArrayList<>();
        List<People> shareholderList = new ArrayList<>();
        List<People> memberList = new ArrayList<>();
        List<People> representativeList = new ArrayList<>();

        QueryWrapper<SmartGroupEconomyPeople> groupQueryWrapper = new QueryWrapper<>();
        groupQueryWrapper.eq("depart", locationId);
        smartGroupEconomyPeopleService.list(groupQueryWrapper).forEach(smartGroupEconomyPeople -> {
            log.info(String.valueOf(smartGroupEconomyPeople));
            if(smartGroupEconomyPeople == null) {
                log.info("smartGroupEconomyPeople is null");
                return;
            }
            People people = new People();
            people.setName(smartGroupEconomyPeople.getName());
            people.setImg(smartGroupEconomyPeople.getPic());
            if(smartGroupEconomyPeople.getJob().equals(VillagePeopleTypeConstant.council)){
                councilList.add(people);
            } else if(smartGroupEconomyPeople.getJob().equals(VillagePeopleTypeConstant.shareholder)){
                shareholderList.add(people);
            } else if(smartGroupEconomyPeople.getJob().equals(VillagePeopleTypeConstant.member)){
                memberList.add(people);
            } else if(smartGroupEconomyPeople.getJob().equals(VillagePeopleTypeConstant.representative)){
                representativeList.add(people);
            }
        });
        wePowerInfoVo.setCouncilList(councilList);
        wePowerInfoVo.setShareholderList(shareholderList);
        wePowerInfoVo.setMemberList(memberList);
        wePowerInfoVo.setRepresentativeList(representativeList);

        return Result.ok(wePowerInfoVo);

    }
}
