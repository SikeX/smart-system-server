package org.jeecg.modules.villageHome.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.villageHome.entity.villageHome;
import org.jeecg.modules.villageHome.entity.villageRelation;
import org.jeecg.modules.villageHome.service.IvHomeService;
import org.jeecg.modules.villageHome.service.IvillageHomeService;
import org.jeecg.modules.villageHome.service.IvillageRelationService;
import org.jeecg.modules.villageHome.vo.vHome;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
* @Description: 乡镇户口表
* @Author: jeecg-boot
* @Date:   2021-12-24
* @Version: V1.0
*/
@Api(tags="乡镇户口表")
@RestController
@RequestMapping("/vHome/vHome")
@Slf4j
public class vHomeController extends JeecgController<vHome, IvHomeService> {
   @Autowired
   private IvillageHomeService villageHomeService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IvillageRelationService villageRelationService;

    @Autowired
    private ISysDepartService sysDepartService;




   /**
   * 导出excel
   *
   * @param request
   * @param vHome
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request,vHome vHome) {
       return super.exportXls(request, vHome, vHome.class, "乡镇户口表");
   }

}
