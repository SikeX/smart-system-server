package org.jeecg.modules.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.LogDTO;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.*;
import org.jeecg.modules.base.mapper.BaseCommonMapper;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysPermissionDataRuleModel;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.export.ExcelExportServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.jeecg.common.constant.CommonConstant.OPERATE_TYPE_6;

@Service
@Slf4j
public class BaseCommonServiceImpl implements BaseCommonService {

    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;

    @Resource
    private BaseCommonMapper baseCommonMapper;

    @Override
    public void addLog(LogDTO logDTO) {
        if(oConvertUtils.isEmpty(logDTO.getId())){
            logDTO.setId(String.valueOf(IdWorker.getId()));
        }
        //保存日志（异常捕获处理，防止数据太大存储失败，导致业务失败）JT-238
        try {
            baseCommonMapper.saveLog(logDTO);
        } catch (Exception e) {
            log.warn(" LogContent length : "+logDTO.getLogContent().length());
            log.warn(e.getMessage());
        }
    }

    @Override
    public void addLog(String logContent, Integer logType, Integer operatetype, LoginUser user ,String filePath) {
        LogDTO sysLog = new LogDTO();
        sysLog.setId(String.valueOf(IdWorker.getId()));
        //注解上的描述,操作日志内容
        sysLog.setLogContent(logContent);
        sysLog.setLogType(logType);
        sysLog.setOperateType(operatetype);
        sysLog.setExportFile(filePath);
        try {
            //获取request
            HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
            //设置IP地址
            sysLog.setIp(IPUtils.getIpAddr(request));
        } catch (Exception e) {
            sysLog.setIp("127.0.0.1");
        }
        //获取登录用户信息
        if(user==null){
            try {
                user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        if(user!=null){
            sysLog.setUserid(user.getUsername());
            sysLog.setUsername(user.getRealname());
        }
        sysLog.setCreateTime(new Date());
        //保存日志（异常捕获处理，防止数据太大存储失败，导致业务失败）JT-238
        try {
            log.info("这里"+String.valueOf(sysLog));
            baseCommonMapper.saveLog(sysLog);
        } catch (Exception e) {
            log.warn(" LogContent length : "+sysLog.getLogContent().length());
            log.warn(e.getMessage());
        }
    }

    @Override
    public void addLog(String logContent, Integer logType, Integer operatetype, LoginUser user) {
        addLog(logContent, logType, operatetype, user, null);
    }

    @Override
    public void addLog(String logContent, Integer logType, Integer operateType) {
        addLog(logContent, logType, operateType, null, null);
    }

    @Override
    public void addExportLog(Map<String, Object> model, String type, HttpServletRequest request,
                             HttpServletResponse response) throws IOException {

        String codedFileName = "临时文件";
        Workbook workbook = null;
        //---update-end-----autor:scott------date:20191016-------for:导出字段支持自定义--------
        String[] exportFields = null;

        Object exportFieldStr =  model.get(NormalExcelConstants.EXPORT_FIELDS);
        if(exportFieldStr!=null && exportFieldStr!=""){
            exportFields = exportFieldStr.toString().split(",");
        }
        //---update-end-----autor:scott------date:20191016-------for:导出字段支持自定义--------
        if (model.containsKey(NormalExcelConstants.MAP_LIST)) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) model.get(NormalExcelConstants.MAP_LIST);
            if (list.size() == 0) {
                throw new RuntimeException("MAP_LIST IS NULL");
            }
            workbook = ExcelExportUtil.exportExcel((ExportParams) list.get(0).get(NormalExcelConstants.PARAMS), (Class<?>) list.get(0).get(NormalExcelConstants.CLASS), (Collection<?>) list.get(0).get(NormalExcelConstants.DATA_LIST),exportFields);
            for (int i = 1; i < list.size(); i++) {
                new ExcelExportServer().createSheet(workbook, (ExportParams) list.get(i).get(NormalExcelConstants.PARAMS), (Class<?>) list.get(i).get(NormalExcelConstants.CLASS), (Collection<?>) list.get(i).get(NormalExcelConstants.DATA_LIST),exportFields);
            }
        } else {
            workbook = ExcelExportUtil.exportExcel((ExportParams) model.get(NormalExcelConstants.PARAMS), (Class<?>) model.get(NormalExcelConstants.CLASS), (Collection<?>) model.get(NormalExcelConstants.DATA_LIST),exportFields);
        }
        if (model.containsKey(NormalExcelConstants.FILE_NAME)) {
            codedFileName = (String) model.get(NormalExcelConstants.FILE_NAME);
        }
//        if (workbook instanceof HSSFWorkbook) {
//            codedFileName += HSSF;
//        } else {
//            codedFileName += XSSF;
//        }
//        if (isIE(request)) {
//            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF8");
//        } else {
//            codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
//        }
        response.setHeader("content-disposition", "attachment;filename=" + codedFileName);
//        ServletOutputStream out = response.getOutputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);

        FileUtil fileUtil = new FileUtil();

        InputStream inputStream = new ByteArrayInputStream(out.toByteArray());

        String fileName = (String) model.get(NormalExcelConstants.FILE_NAME) + ".xls";

        MultipartFile multi = new CommonsMultipartFile(fileUtil.createFileItem(inputStream,fileName));

        String filePath = CommonUtils.uploadLocal(multi,"Export",uploadpath);

        log.info("这里"+filePath);

        addLog(type + "列表-导出", CommonConstant.LOG_TYPE_2, OPERATE_TYPE_6,null, filePath);

//        out.flush();

    }



}
