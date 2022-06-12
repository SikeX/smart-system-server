package org.jeecg.modules.SmartPaper.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.SmartPaper.entity.SmartExamInformation;
import org.jeecg.modules.SmartPaper.entity.SmartPaper;
import org.jeecg.modules.SmartPaper.service.ISmartExamInfoService;
import org.jeecg.modules.SmartPaper.service.ISmartMyExamService;
import org.jeecg.modules.SmartPaper.service.ISmartPaperService;
import org.jeecg.modules.SmartPaper.service.ISmartPeopleService;
import org.jeecg.modules.SmartPaper.vo.DeptExamRankVo;
import org.jeecg.modules.SmartPaper.vo.ExamPeopleScoreVo;
import org.jeecg.modules.SmartPaper.vo.SmartMyExamVo;
import org.jeecg.modules.common.util.ParamsUtil;
import org.jeecg.modules.smartEvaluateList.entity.peopleAvg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
* @Description: 考试信息表
* @Author: jeecg-boot
* @Date:   2021-11-30
* @Version: V1.0
*/
@Api(tags="考试信息表")
@RestController
@RequestMapping("/SmartPaper/smartMyExam")
@Slf4j
public class SmartExamInfoController extends JeecgController<SmartExamInformation, ISmartExamInfoService> {
   @Autowired
   private ISmartExamInfoService smartExamInformationService;
   @Autowired
   private ISmartPeopleService smartPeopleService;
   @Autowired
   private ISmartMyExamService smartMyExamService;
   @Autowired
   private ISmartPaperService smartPaperService;

   /**
    * 分页列表查询
    *
    * @param smartExamInformation
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "考试信息表-分页列表查询")
   @ApiOperation(value="考试信息表-分页列表查询", notes="考试信息表-分页列表查询")
   @GetMapping(value = "/list")
   public Result<?> queryPageList(SmartExamInformation smartExamInformation,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<SmartExamInformation> queryWrapper = QueryGenerator.initQueryWrapper(smartExamInformation, req.getParameterMap());
       /*String oldExamName = smartExamInformation.getExamName();
       String examName = "";
       if(oldExamName == null || oldExamName.equals("")){
           examName = oldExamName;
       }else {
           examName = oldExamName.replace('*','%');
       }*/
       Page<SmartExamInformation> page = new Page<SmartExamInformation>(pageNo, pageSize);
       //IPage<SmartExamInformation> pageList = smartExamInformationService.getAllExam(page,examId);
       IPage<SmartExamInformation> pageList = smartExamInformationService.page(page,queryWrapper);
       return Result.OK(pageList);
   }
    @AutoLog(value = "个人考试信息表-分页列表查询")
    @ApiOperation(value="个人考试信息表-分页列表查询", notes="个人考试信息表-分页列表查询")
    @GetMapping(value = "/myList")
    public Result<?> queryPageMyList(SmartMyExamVo smartMyExamVo,
                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                     HttpServletRequest req) {
        // 1. 规则，下面是 以**开始
        String rule = "in";
        // 2. 查询字段
        String field = "id";
        // 获取登录用户信息,查询该用户考试
        LoginUser sysUser =  (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        String examIds = smartPeopleService.getMyExamString(userId);

        HashMap<String, String[]> map = new HashMap<>(req.getParameterMap());
        // 获取请求参数中的superQueryParams
        List<String> paramsList = ParamsUtil.getSuperQueryParams(req.getParameterMap());
        // 添加额外查询条件，用于权限控制
        paramsList.add("%5B%7B%22rule%22:%22" + rule + "%22,%22type%22:%22string%22,%22dictCode%22:%22%22,%22val%22:%22"
                + examIds
                + "%22,%22field%22:%22" + field + "%22%7D%5D");

        String[] params = new String[paramsList.size()];
        paramsList.toArray(params);
        map.put("superQueryParams", params);
        params = new String[]{"and"};
        map.put("superQueryMatchType", params);

        QueryWrapper<SmartMyExamVo> queryWrapper = QueryGenerator.initQueryWrapper(smartMyExamVo, map);

        Page<SmartMyExamVo> page = new Page<SmartMyExamVo>(pageNo, pageSize);
        String oldExamName = smartMyExamVo.getExamName();
        String examName = "";
        if(oldExamName == null || oldExamName.equals("")){
            examName = oldExamName;
        }else {
            examName = oldExamName.replace('*','%');
        }
        System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
        System.out.println(req.getParameterMap());
        System.out.println(oldExamName);
        System.out.println(examName);
        IPage<SmartMyExamVo> pageList = smartMyExamService.getMyAllExam(page,userId,examName);
        return Result.OK(pageList);
    }

   /**
    *   添加
    *
    * @param smartExamInformation
    * @return
    */
   @AutoLog(value = "考试信息表-添加")
   @ApiOperation(value="考试信息表-添加", notes="考试信息表-添加")
   @PostMapping(value = "/add")
   public Result<?> add(@RequestBody SmartExamInformation smartExamInformation) {
       smartExamInformationService.save(smartExamInformation);
       return Result.OK("添加成功！");
   }

   /**
    *  编辑
    *
    * @param smartExamInformation
    * @return
    */
   @AutoLog(value = "考试信息表-编辑")
   @ApiOperation(value="考试信息表-编辑", notes="考试信息表-编辑")
   @PutMapping(value = "/edit")
   public Result<?> edit(@RequestBody SmartExamInformation smartExamInformation) {
       smartExamInformationService.updateById(smartExamInformation);
       return Result.OK("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "考试信息表-通过id删除")
   @ApiOperation(value="考试信息表-通过id删除", notes="考试信息表-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<?> delete(@RequestParam(name="id",required=true) String id) {
       smartExamInformationService.removeById(id);
       return Result.OK("删除成功!");
   }
    @AutoLog(value = "考试信息表-取消发布")
    @ApiOperation(value="考试信息表-取消发布", notes="考试信息表-取消发布")
    @PostMapping(value = "/deleteRelease")
    public Result<?> deleteRelease(@RequestBody SmartExamInformation smartExamInformation) {
       //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        //System.out.println(smartExamInformation);
        String id = smartExamInformation.getId();
        String paperId = smartExamInformation.getPaperId();
        smartExamInformationService.removeById(id);
        SmartPaper smartPaper = new SmartPaper();
        smartPaper.setId(paperId);
        smartPaper.setPaperStatus("0");
        smartPaperService.updateById(smartPaper);
        return Result.OK("成功!");
    }
   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "考试信息表-批量删除")
   @ApiOperation(value="考试信息表-批量删除", notes="考试信息表-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.smartExamInformationService.removeByIds(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功!");
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   @AutoLog(value = "考试信息表-通过id查询")
   @ApiOperation(value="考试信息表-通过id查询", notes="考试信息表-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
       SmartExamInformation smartExamInformation = smartExamInformationService.getById(id);
       if(smartExamInformation==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(smartExamInformation);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param smartExamInformation
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, SmartExamInformation smartExamInformation) {
       return super.exportXls(request, smartExamInformation, SmartExamInformation.class, "考试信息表");
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
       return super.importExcel(request, response, SmartExamInformation.class);
   }


    /**
     *考试-单位排名
     *
     * @param
     * @return
     */
    @AutoLog(value = "考试-单位排名")
    @ApiOperation(value="考试-单位排名", notes="考试-单位排名")
    @GetMapping(value = "/deptExamRank")
    public Result<?> deptExamRank(DeptExamRankVo deptExamRankVo,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
        try {
            String examId = deptExamRankVo.getExamId();
            //System.out.println("###############################");
           // System.out.println(examId);
            if (examId != null && !examId.isEmpty()) {
                if (examId.startsWith(",")) {
                    System.out.println("process");
                    examId = examId.substring(1);
                }
            }
            Page<DeptExamRankVo> page = new Page<DeptExamRankVo>(pageNo, pageSize);
            IPage<DeptExamRankVo> pageList = smartExamInformationService.deptExamRank(page,examId);
            return Result.OK(pageList);
        }catch (Exception e){
            return Result.error("error");
        }
    }

    /**
     *考试-单位排名
     *
     * @param
     * @return
     */
    @AutoLog(value = "考试-个人排名")
    @ApiOperation(value="考试-个人排名", notes="考试-个人排名")
    @GetMapping(value = "/peopleExamRank")
    public Result<?> peopleExamRank(ExamPeopleScoreVo examPeopleScoreVo,
                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                    HttpServletRequest req) {
        try {
            String examId = examPeopleScoreVo.getExamId();
            //System.out.println("###############################");
            // System.out.println(examId);
            if (examId != null && !examId.isEmpty()) {
                if (examId.startsWith(",")) {
                    System.out.println("process");
                    examId = examId.substring(1);
                }
            }
            Page<ExamPeopleScoreVo> page = new Page<ExamPeopleScoreVo>(pageNo, pageSize);
            IPage<ExamPeopleScoreVo> pageList = smartExamInformationService.peopleExamRank(page,examId);
            return Result.OK(pageList);
        }catch (Exception e){
            return Result.error("error");
        }
    }
}
