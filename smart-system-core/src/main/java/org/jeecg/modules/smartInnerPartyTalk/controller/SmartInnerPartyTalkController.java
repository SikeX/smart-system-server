package org.jeecg.modules.SmartInnerPartyTalk.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.SmartInnerPartyTalk.entity.SmartInnerPartyAnnex;
import org.jeecg.modules.SmartInnerPartyTalk.entity.SmartInnerPartyPacpa;
import org.jeecg.modules.SmartInnerPartyTalk.entity.SmartInnerPartyTalk;
import org.jeecg.modules.SmartInnerPartyTalk.service.ISmartInnerPartyAnnexService;
import org.jeecg.modules.SmartInnerPartyTalk.service.ISmartInnerPartyPacpaService;
import org.jeecg.modules.SmartInnerPartyTalk.service.ISmartInnerPartyTalkService;
import org.jeecg.modules.SmartInnerPartyTalk.vo.SmartInnerPartyTalkPage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
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
 * @Description: 党内谈话表
 * @Author: jeecg-boot
 * @Date:   2021-11-10
 * @Version: V1.0
 */
@Api(tags="党内谈话表")
@RestController
@RequestMapping("/SmartInnerPartyTalk/smartInnerPartyTalk")
@Slf4j
public class SmartInnerPartyTalkController {
	@Autowired
	private ISmartInnerPartyTalkService smartInnerPartyTalkService;
	@Autowired
	private ISmartInnerPartyPacpaService smartInnerPartyPacpaService;
	@Autowired
	private ISmartInnerPartyAnnexService smartInnerPartyAnnexService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smartInnerPartyTalk
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "党内谈话表-分页列表查询")
	@ApiOperation(value="党内谈话表-分页列表查询", notes="党内谈话表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartInnerPartyTalk smartInnerPartyTalk,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		// TODO：1. 规则，下面是 以＊*开始
		String rule = "right_like";
		// TODO：2. 查询字段
		String field = "departId";
		// 获取登录用户信息，可以用来查询单位部门信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		System.out.println(sysUser.getOrgCode());

		// 添加查询参数，下面的参数是查询以用户所在部门编码开头的的所有单位数据，即用户所在单位和子单位的信息
		// superQueryParams=[{"rule":"right_like","type":"string","dictCode":"","val":"用户所在的部门","field":"departId"}]
		HashMap<String, String[]> map = new HashMap<>(req.getParameterMap());
		String[] params = {"%5B%7B%22rule%22:%22" + rule + "%22,%22type%22:%22string%22,%22dictCode%22:%22%22,%22val%22:%22"
				+ sysUser.getOrgCode()
				+ "%22,%22field%22:%22" + field + "%22%7D%5D"};
		map.put("superQueryParams", params);
		params = new String[]{"and"};
		map.put("superQueryMatchType", params);
		QueryWrapper<SmartInnerPartyTalk> queryWrapper = QueryGenerator.initQueryWrapper(smartInnerPartyTalk, map);
		Page<SmartInnerPartyTalk> page = new Page<SmartInnerPartyTalk>(pageNo, pageSize);
		IPage<SmartInnerPartyTalk> pageList = smartInnerPartyTalkService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param smartInnerPartyTalkPage
	 * @return
	 */
	@AutoLog(value = "党内谈话表-添加")
	@ApiOperation(value="党内谈话表-添加", notes="党内谈话表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartInnerPartyTalkPage smartInnerPartyTalkPage) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String orgCode = sysUser.getOrgCode();
		if ("".equals(orgCode)) {
			return Result.error("本用户没有操作权限！");
		}
		String id = smartInnerPartyTalkService.getDepartIdByOrgCode(orgCode);
		smartInnerPartyTalkPage.setDepartId(id);

		SmartInnerPartyTalk smartInnerPartyTalk = new SmartInnerPartyTalk();
		BeanUtils.copyProperties(smartInnerPartyTalkPage, smartInnerPartyTalk);
		smartInnerPartyTalkService.saveMain(smartInnerPartyTalk, smartInnerPartyTalkPage.getSmartInnerPartyPacpaList(),smartInnerPartyTalkPage.getSmartInnerPartyAnnexList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param smartInnerPartyTalkPage
	 * @return
	 */
	@AutoLog(value = "党内谈话表-编辑")
	@ApiOperation(value="党内谈话表-编辑", notes="党内谈话表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartInnerPartyTalkPage smartInnerPartyTalkPage) {
		SmartInnerPartyTalk smartInnerPartyTalk = new SmartInnerPartyTalk();
		BeanUtils.copyProperties(smartInnerPartyTalkPage, smartInnerPartyTalk);
		SmartInnerPartyTalk smartInnerPartyTalkEntity = smartInnerPartyTalkService.getById(smartInnerPartyTalk.getId());
		if(smartInnerPartyTalkEntity==null) {
			return Result.error("未找到对应数据");
		}
		smartInnerPartyTalkService.updateMain(smartInnerPartyTalk, smartInnerPartyTalkPage.getSmartInnerPartyPacpaList(),smartInnerPartyTalkPage.getSmartInnerPartyAnnexList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "党内谈话表-通过id删除")
	@ApiOperation(value="党内谈话表-通过id删除", notes="党内谈话表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		smartInnerPartyTalkService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "党内谈话表-批量删除")
	@ApiOperation(value="党内谈话表-批量删除", notes="党内谈话表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartInnerPartyTalkService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "党内谈话表-通过id查询")
	@ApiOperation(value="党内谈话表-通过id查询", notes="党内谈话表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartInnerPartyTalk smartInnerPartyTalk = smartInnerPartyTalkService.getById(id);
		if(smartInnerPartyTalk==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartInnerPartyTalk);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "党内谈话参与人表通过主表ID查询")
	@ApiOperation(value="党内谈话参与人表主表ID查询", notes="党内谈话参与人表-通主表ID查询")
	@GetMapping(value = "/querySmartInnerPartyPacpaByMainId")
	public Result<?> querySmartInnerPartyPacpaListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SmartInnerPartyPacpa> smartInnerPartyPacpaList = smartInnerPartyPacpaService.selectByMainId(id);
		return Result.OK(smartInnerPartyPacpaList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "党内谈话附件表通过主表ID查询")
	@ApiOperation(value="党内谈话附件表主表ID查询", notes="党内谈话附件表-通主表ID查询")
	@GetMapping(value = "/querySmartInnerPartyAnnexByMainId")
	public Result<?> querySmartInnerPartyAnnexListByMainId(@RequestParam(name="id",required=true) String id) {
		List<SmartInnerPartyAnnex> smartInnerPartyAnnexList = smartInnerPartyAnnexService.selectByMainId(id);
		return Result.OK(smartInnerPartyAnnexList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartInnerPartyTalk
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartInnerPartyTalk smartInnerPartyTalk) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<SmartInnerPartyTalk> queryWrapper = QueryGenerator.initQueryWrapper(smartInnerPartyTalk, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<SmartInnerPartyTalk> queryList = smartInnerPartyTalkService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<SmartInnerPartyTalk> smartInnerPartyTalkList = new ArrayList<SmartInnerPartyTalk>();
      if(oConvertUtils.isEmpty(selections)) {
          smartInnerPartyTalkList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          smartInnerPartyTalkList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<SmartInnerPartyTalkPage> pageList = new ArrayList<SmartInnerPartyTalkPage>();
      for (SmartInnerPartyTalk main : smartInnerPartyTalkList) {
          SmartInnerPartyTalkPage vo = new SmartInnerPartyTalkPage();
          BeanUtils.copyProperties(main, vo);
          List<SmartInnerPartyPacpa> smartInnerPartyPacpaList = smartInnerPartyPacpaService.selectByMainId(main.getId());
          vo.setSmartInnerPartyPacpaList(smartInnerPartyPacpaList);
          List<SmartInnerPartyAnnex> smartInnerPartyAnnexList = smartInnerPartyAnnexService.selectByMainId(main.getId());
          vo.setSmartInnerPartyAnnexList(smartInnerPartyAnnexList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "党内谈话表列表");
      mv.addObject(NormalExcelConstants.CLASS, SmartInnerPartyTalkPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("党内谈话表数据", "导出人:"+sysUser.getRealname(), "党内谈话表"));
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
              List<SmartInnerPartyTalkPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartInnerPartyTalkPage.class, params);
              for (SmartInnerPartyTalkPage page : list) {
                  SmartInnerPartyTalk po = new SmartInnerPartyTalk();
                  BeanUtils.copyProperties(page, po);
                  smartInnerPartyTalkService.saveMain(po, page.getSmartInnerPartyPacpaList(),page.getSmartInnerPartyAnnexList());
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
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

	 @AutoLog(value = "更新文件下载次数")
	 @ApiOperation(value="更新文件下载次数", notes="更新文件下载次数")
	 @PutMapping(value = "/downloadCount")
	 public Result<?> edit(@RequestBody SmartInnerPartyAnnex smartInnerPartyAnnex) {
		 SmartInnerPartyAnnex newSmartInnerPartyAnnex =
				 smartInnerPartyAnnexService.getById(smartInnerPartyAnnex.getId());
		 int currentCount = newSmartInnerPartyAnnex.getDownloadCount();
		 newSmartInnerPartyAnnex.setDownloadCount(currentCount+1);
		 smartInnerPartyAnnexService.updateById(newSmartInnerPartyAnnex);
		 return Result.OK("更新成功!");
	 }

}
