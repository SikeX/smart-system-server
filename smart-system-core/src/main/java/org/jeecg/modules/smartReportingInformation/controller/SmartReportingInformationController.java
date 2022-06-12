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
 * @Description: 举报信息表
 * @Author: jeecg-boot
 * @Date: 2021-11-29
 * @Version: V1.0
 */
@Api(tags = "举报信息表")
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
     * 分页列表查询
     *
     * @param smartReportingInformation
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "举报信息表-分页列表查询")
    @ApiOperation(value = "举报信息表-分页列表查询", notes = "举报信息表-分页列表查询")
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
     * 添加
     *
     * @param smartReportingInformationPage
     * @return
     */
    @AutoLog(value = "举报信息表-添加")
    @ApiOperation(value = "举报信息表-添加", notes = "举报信息表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartReportingInformationPage smartReportingInformationPage) {
        SmartReportingInformation smartReportingInformation = new SmartReportingInformation();

        BeanUtils.copyProperties(smartReportingInformationPage, smartReportingInformation);

        smartReportingInformation.setReportingTime(new Date());

        log.info(String.valueOf(smartReportingInformation));
        smartReportingInformationService.saveMain(smartReportingInformation, smartReportingInformationPage.getSmartReportingSurveyList(), smartReportingInformationPage.getSmartReportingDescriptionList());
        // 2021-12-12 新增websocket通知 @Author CabbSir @TODO 不能写死
        List<String> list = sysBaseAPI.getSysUserListByRole("1464894241405718530");
        sysBaseAPI.sendWebSocketMsg(list.toArray(new String[list.size()]), WebsocketConst.CMD_REPORT);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param smartReportingInformationPage
     * @return
     */
    @AutoLog(value = "举报信息表-编辑")
    @ApiOperation(value = "举报信息表-编辑", notes = "举报信息表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartReportingInformationPage smartReportingInformationPage) {
        SmartReportingInformation smartReportingInformation = new SmartReportingInformation();
        BeanUtils.copyProperties(smartReportingInformationPage, smartReportingInformation);
        SmartReportingInformation smartReportingInformationEntity = smartReportingInformationService.getById(smartReportingInformation.getId());
        if (smartReportingInformationEntity == null) {
            return Result.error("未找到对应数据");
        }
        smartReportingInformationService.updateMain(smartReportingInformation, smartReportingInformationPage.getSmartReportingSurveyList(), smartReportingInformationPage.getSmartReportingDescriptionList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "举报信息表-通过id删除")
    @ApiOperation(value = "举报信息表-通过id删除", notes = "举报信息表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        smartReportingInformationService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "举报信息表-批量删除")
    @ApiOperation(value = "举报信息表-批量删除", notes = "举报信息表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.smartReportingInformationService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "举报信息表-通过id查询")
    @ApiOperation(value = "举报信息表-通过id查询", notes = "举报信息表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SmartReportingInformation smartReportingInformation = smartReportingInformationService.getById(id);
        if (smartReportingInformation == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(smartReportingInformation);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "举报调查表通过主表ID查询")
    @ApiOperation(value = "举报调查表主表ID查询", notes = "举报调查表-通主表ID查询")
    @GetMapping(value = "/querySmartReportingSurveyByMainId")
    public Result<?> querySmartReportingSurveyListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<SmartReportingSurvey> smartReportingSurveyList = smartReportingSurveyService.selectByMainId(id);
        return Result.OK(smartReportingSurveyList);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "举报附件表通过主表ID查询")
    @ApiOperation(value = "举报附件表主表ID查询", notes = "举报附件表-通主表ID查询")
    @GetMapping(value = "/querySmartReportingDescriptionByMainId")
    public Result<?> querySmartReportingDescriptionListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<SmartReportingDescription> smartReportingDescriptionList = smartReportingDescriptionService.selectByMainId(id);
        return Result.OK(smartReportingDescriptionList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param smartReportingInformation
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartReportingInformation smartReportingInformation) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<SmartReportingInformation> queryWrapper = QueryGenerator.initQueryWrapper(smartReportingInformation, request.getParameterMap());
//		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = JwtUtil.getUserNameByToken(request);
        //Step.2 获取导出数据
        List<SmartReportingInformation> queryList = smartReportingInformationService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<SmartReportingInformation> smartReportingInformationList = new ArrayList<SmartReportingInformation>();
        if (oConvertUtils.isEmpty(selections)) {
            smartReportingInformationList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            smartReportingInformationList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
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

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "举报信息表列表");
        mv.addObject(NormalExcelConstants.CLASS, SmartReportingInformationPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("举报信息表数据", "导出人:" + username, "举报信息表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
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
            MultipartFile file = entity.getValue();// 获取上传文件对象
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
                return Result.OK("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.OK("文件导入失败！");
    }


    //发送消息
    @PostMapping(value = "/sendMessage")
    private Result<?> sendMessage(@RequestBody SmartReportingInformationPage smartReportingInformationPage) {

        //LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String content = "您的举报信息已提交";
        String receiverPhone = smartReportingInformationPage.getContactNumber();
        DySmsHelper.sendSms(content, receiverPhone);
        return Result.OK("短信发送成功");
    }

    @PostMapping(value = "/sendMessageAgree")
    private Result<?> sendMessageAgree(@RequestBody SmartReportingInformationPage smartReportingInformationPage) {

        //LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String content = "您的举报信息已受理";
        String receiverPhone = smartReportingInformationPage.getContactNumber();
        DySmsHelper.sendSms(content, receiverPhone);
        return Result.OK("短信发送成功");
    }

    @PostMapping(value = "/sendMessageDisagree")
    private Result<?> sendMessageDisagree(@RequestBody SmartReportingInformationPage smartReportingInformationPage) {

        //LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//登录用户
        String content = "您的举报信息未成功";
        String receiverPhone = smartReportingInformationPage.getContactNumber();
        DySmsHelper.sendSms(content, receiverPhone);
        return Result.OK("短信发送成功");

    }

    @PostMapping(value = "/sendMessageFinish")
    private Result<?> sendMessageFinish(@RequestBody SmartReportingInformationPage smartReportingInformationPage) {

        //LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//登录用户
        String content = "你的举报信息已处理完结";
        String receiverPhone = smartReportingInformationPage.getContactNumber();
        DySmsHelper.sendSms(content, receiverPhone);
        return Result.OK("短信发送成功");

    }

}
