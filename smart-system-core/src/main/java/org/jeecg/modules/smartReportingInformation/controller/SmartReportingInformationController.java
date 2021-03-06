package org.jeecg.modules.smartReportingInformation.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.jeecg.common.constant.WebsocketConst;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.smartReportingInformation.entity.SmartReportingSurvey;
import org.jeecg.modules.smartReportingInformation.entity.SmartReportingDescription;
import org.jeecg.modules.smartReportingInformation.entity.SmartReportingInformation;
import org.jeecg.modules.smartReportingInformation.vo.SmartReportingInformationPage;
import org.jeecg.modules.smartReportingInformation.service.ISmartReportingInformationService;
import org.jeecg.modules.smartReportingInformation.service.ISmartReportingSurveyService;
import org.jeecg.modules.smartReportingInformation.service.ISmartReportingDescriptionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

import org.jeecg.common.util.DySmsHelper;

/**
 * @Description: ???????????????
 * @Author: jeecg-boot
 * @Date: 2021-11-29
 * @Version: V1.0
 */
@Api(tags = "???????????????")
@RestController
@RequestMapping("/smartReportingInformation/smartReportingInformation")
@Slf4j
public class SmartReportingInformationController {
    @Autowired
    private ISmartReportingInformationService smartReportingInformationService;
    @Autowired
    private ISmartReportingSurveyService smartReportingSurveyService;
    @Autowired
    private ISmartReportingDescriptionService smartReportingDescriptionService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;

    /**
     * ??????????????????
     *
     * @param smartReportingInformation
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "???????????????-??????????????????")
    @ApiOperation(value = "???????????????-??????????????????", notes = "???????????????-??????????????????")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SmartReportingInformation smartReportingInformation,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SmartReportingInformation> queryWrapper = QueryGenerator.initQueryWrapper(smartReportingInformation, req.getParameterMap());
        Page<SmartReportingInformation> page = new Page<SmartReportingInformation>(pageNo, pageSize);
        IPage<SmartReportingInformation> pageList = smartReportingInformationService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * ??????
     *
     * @param smartReportingInformationPage
     * @return
     */
    @AutoLog(value = "???????????????-??????")
    @ApiOperation(value = "???????????????-??????", notes = "???????????????-??????")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartReportingInformationPage smartReportingInformationPage) {
        SmartReportingInformation smartReportingInformation = new SmartReportingInformation();

        BeanUtils.copyProperties(smartReportingInformationPage, smartReportingInformation);

        smartReportingInformation.setReportingTime(new Date());

        log.info(String.valueOf(smartReportingInformation));
        smartReportingInformationService.saveMain(smartReportingInformation, smartReportingInformationPage.getSmartReportingSurveyList(), smartReportingInformationPage.getSmartReportingDescriptionList());
        // 2021-12-12 ??????websocket?????? @Author CabbSir @TODO ????????????
        List<String> list = sysBaseAPI.getSysUserListByRole("1464894241405718530");
        sysBaseAPI.sendWebSocketMsg(list.toArray(new String[list.size()]), WebsocketConst.CMD_REPORT);
        return Result.OK("???????????????");
    }

    /**
     * ??????
     *
     * @param smartReportingInformationPage
     * @return
     */
    @AutoLog(value = "???????????????-??????")
    @ApiOperation(value = "???????????????-??????", notes = "???????????????-??????")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartReportingInformationPage smartReportingInformationPage) {
        SmartReportingInformation smartReportingInformation = new SmartReportingInformation();
        BeanUtils.copyProperties(smartReportingInformationPage, smartReportingInformation);
        SmartReportingInformation smartReportingInformationEntity = smartReportingInformationService.getById(smartReportingInformation.getId());
        if (smartReportingInformationEntity == null) {
            return Result.error("?????????????????????");
        }
        smartReportingInformationService.updateMain(smartReportingInformation, smartReportingInformationPage.getSmartReportingSurveyList(), smartReportingInformationPage.getSmartReportingDescriptionList());
        return Result.OK("????????????!");
    }

    /**
     * ??????id??????
     *
     * @param id
     * @return
     */
    @AutoLog(value = "???????????????-??????id??????")
    @ApiOperation(value = "???????????????-??????id??????", notes = "???????????????-??????id??????")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        smartReportingInformationService.delMain(id);
        return Result.OK("????????????!");
    }

    /**
     * ????????????
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "???????????????-????????????")
    @ApiOperation(value = "???????????????-????????????", notes = "???????????????-????????????")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.smartReportingInformationService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("?????????????????????");
    }

    /**
     * ??????id??????
     *
     * @param id
     * @return
     */
    @AutoLog(value = "???????????????-??????id??????")
    @ApiOperation(value = "???????????????-??????id??????", notes = "???????????????-??????id??????")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SmartReportingInformation smartReportingInformation = smartReportingInformationService.getById(id);
        if (smartReportingInformation == null) {
            return Result.error("?????????????????????");
        }
        return Result.OK(smartReportingInformation);

    }

    /**
     * ??????id??????
     *
     * @param id
     * @return
     */
    @AutoLog(value = "???????????????????????????ID??????")
    @ApiOperation(value = "?????????????????????ID??????", notes = "???????????????-?????????ID??????")
    @GetMapping(value = "/querySmartReportingSurveyByMainId")
    public Result<?> querySmartReportingSurveyListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<SmartReportingSurvey> smartReportingSurveyList = smartReportingSurveyService.selectByMainId(id);
        return Result.OK(smartReportingSurveyList);
    }

    /**
     * ??????id??????
     *
     * @param id
     * @return
     */
    @AutoLog(value = "???????????????????????????ID??????")
    @ApiOperation(value = "?????????????????????ID??????", notes = "???????????????-?????????ID??????")
    @GetMapping(value = "/querySmartReportingDescriptionByMainId")
    public Result<?> querySmartReportingDescriptionListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<SmartReportingDescription> smartReportingDescriptionList = smartReportingDescriptionService.selectByMainId(id);
        return Result.OK(smartReportingDescriptionList);
    }

    /**
     * ??????excel
     *
     * @param request
     * @param smartReportingInformation
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartReportingInformation smartReportingInformation) {
        // Step.1 ??????????????????????????????
        QueryWrapper<SmartReportingInformation> queryWrapper = QueryGenerator.initQueryWrapper(smartReportingInformation, request.getParameterMap());
//		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = JwtUtil.getUserNameByToken(request);
        //Step.2 ??????????????????
        List<SmartReportingInformation> queryList = smartReportingInformationService.list(queryWrapper);
        // ??????????????????
        String selections = request.getParameter("selections");
        List<SmartReportingInformation> smartReportingInformationList = new ArrayList<SmartReportingInformation>();
        if (oConvertUtils.isEmpty(selections)) {
            smartReportingInformationList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            smartReportingInformationList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 ??????pageList
        List<SmartReportingInformationPage> pageList = new ArrayList<SmartReportingInformationPage>();
        for (SmartReportingInformation main : smartReportingInformationList) {
            SmartReportingInformationPage vo = new SmartReportingInformationPage();
            BeanUtils.copyProperties(main, vo);
            List<SmartReportingSurvey> smartReportingSurveyList = smartReportingSurveyService.selectByMainId(main.getId());
            vo.setSmartReportingSurveyList(smartReportingSurveyList);
            List<SmartReportingDescription> smartReportingDescriptionList = smartReportingDescriptionService.selectByMainId(main.getId());
            vo.setSmartReportingDescriptionList(smartReportingDescriptionList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi ??????Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "?????????????????????");
        mv.addObject(NormalExcelConstants.CLASS, SmartReportingInformationPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("?????????????????????", "?????????:" + username, "???????????????"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * ??????excel????????????
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// ????????????????????????
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<SmartReportingInformationPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartReportingInformationPage.class, params);
                for (SmartReportingInformationPage page : list) {
                    SmartReportingInformation po = new SmartReportingInformation();
                    BeanUtils.copyProperties(page, po);
                    smartReportingInformationService.saveMain(po, page.getSmartReportingSurveyList(), page.getSmartReportingDescriptionList());
                }
                return Result.OK("?????????????????????????????????:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("??????????????????:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.OK("?????????????????????");
    }


    //????????????
    @PostMapping(value = "/sendMessage")
    private Result<?> sendMessage(@RequestBody SmartReportingInformationPage smartReportingInformationPage) {

        //LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String content = "???????????????????????????";
        String receiverPhone = smartReportingInformationPage.getContactNumber();
        DySmsHelper.sendSms(content, receiverPhone);
        return Result.OK("??????????????????");
    }

    @PostMapping(value = "/sendMessageAgree")
    private Result<?> sendMessageAgree(@RequestBody SmartReportingInformationPage smartReportingInformationPage) {

        //LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String content = "???????????????????????????";
        String receiverPhone = smartReportingInformationPage.getContactNumber();
        DySmsHelper.sendSms(content, receiverPhone);
        return Result.OK("??????????????????");
    }

    @PostMapping(value = "/sendMessageDisagree")
    private Result<?> sendMessageDisagree(@RequestBody SmartReportingInformationPage smartReportingInformationPage) {

        //LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//????????????
        String content = "???????????????????????????";
        String receiverPhone = smartReportingInformationPage.getContactNumber();
        DySmsHelper.sendSms(content, receiverPhone);
        return Result.OK("??????????????????");

    }

    @PostMapping(value = "/sendMessageFinish")
    private Result<?> sendMessageFinish(@RequestBody SmartReportingInformationPage smartReportingInformationPage) {

        //LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//????????????
        String content = "?????????????????????????????????";
        String receiverPhone = smartReportingInformationPage.getContactNumber();
        DySmsHelper.sendSms(content, receiverPhone);
        return Result.OK("??????????????????");

    }

}
