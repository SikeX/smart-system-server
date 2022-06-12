package org.jeecg.modules.wePower.smartGroupEconomy.controller;

import cn.hutool.extra.pinyin.PinyinUtil;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.system.query.QueryGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.utils.FaceRecognitionUtil;
import org.jeecg.modules.utils.ImageUtils;
import org.jeecg.modules.utils.UrlUtil;
import org.jeecg.modules.wePower.smartEvadeRelation.entity.SmartEvadeRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import java.util.Arrays;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.wePower.smartGroupEconomy.entity.SmartGroupEconomyPeople;
import org.jeecg.modules.wePower.smartGroupEconomy.entity.SmartGroupEconomyMeeting;
import org.jeecg.modules.wePower.smartGroupEconomy.entity.SmartGroupEconomy;
import org.jeecg.modules.wePower.smartGroupEconomy.service.ISmartGroupEconomyService;
import org.jeecg.modules.wePower.smartGroupEconomy.service.ISmartGroupEconomyPeopleService;
import org.jeecg.modules.wePower.smartGroupEconomy.service.ISmartGroupEconomyMeetingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

 /**
 * @Description: 农村集体经济组织
 * @Author: jeecg-boot
 * @Date:   2022-03-06
 * @Version: V1.0
 */
@Api(tags="农村集体经济组织")
@RestController
@RequestMapping("/smartGroupEconomy/smartGroupEconomy")
@Slf4j
public class SmartGroupEconomyController extends JeecgController<SmartGroupEconomy, ISmartGroupEconomyService> {

	@Autowired
	private ISmartGroupEconomyService smartGroupEconomyService;

	@Autowired
	private ISmartGroupEconomyPeopleService smartGroupEconomyPeopleService;

	@Autowired
	private ISmartGroupEconomyMeetingService smartGroupEconomyMeetingService;

	@Value("${jeecg.fileBaseUrl}")
	private String fileBaseUrl;

	private String groupId = "GroupEconomy";


	/*---------------------------------主表处理-begin-------------------------------------*/

	/**
	 * 分页列表查询
	 * @param smartGroupEconomy
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "农村集体经济组织-分页列表查询")
	@ApiOperation(value="农村集体经济组织-分页列表查询", notes="农村集体经济组织-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartGroupEconomy smartGroupEconomy,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartGroupEconomy> queryWrapper = QueryGenerator.initQueryWrapper(smartGroupEconomy, req.getParameterMap());
		Page<SmartGroupEconomy> page = new Page<SmartGroupEconomy>(pageNo, pageSize);
		IPage<SmartGroupEconomy> pageList = smartGroupEconomyService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @AutoLog(value = "农村集体经济组织-分页列表查询")
	 @ApiOperation(value="农村集体经济组织-分页列表查询", notes="农村集体经济组织-分页列表查询")
	 @GetMapping(value = "/listAdmin")
	 public Result<?> queryPageListAdmin(SmartGroupEconomy smartGroupEconomy,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {

		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String orgCode = sysUser.getOrgCode();
		 if ("".equals(orgCode)) {
			 return Result.error("本用户没有操作权限！");
		 }
		 QueryWrapper<SmartGroupEconomy> queryWrapper = QueryGenerator.initQueryWrapper(smartGroupEconomy, req.getParameterMap());
		 queryWrapper.eq("sys_org_code", orgCode);
		 Page<SmartGroupEconomy> page = new Page<SmartGroupEconomy>(pageNo, pageSize);
		 IPage<SmartGroupEconomy> pageList = smartGroupEconomyService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	/**
     *   添加
     * @param smartGroupEconomy
     * @return
     */
    @AutoLog(value = "农村集体经济组织-添加")
    @ApiOperation(value="农村集体经济组织-添加", notes="农村集体经济组织-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartGroupEconomy smartGroupEconomy) {
        smartGroupEconomyService.save(smartGroupEconomy);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     * @param smartGroupEconomy
     * @return
     */
    @AutoLog(value = "农村集体经济组织-编辑")
    @ApiOperation(value="农村集体经济组织-编辑", notes="农村集体经济组织-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartGroupEconomy smartGroupEconomy) {
        smartGroupEconomyService.updateById(smartGroupEconomy);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "农村集体经济组织-通过id删除")
    @ApiOperation(value="农村集体经济组织-通过id删除", notes="农村集体经济组织-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        smartGroupEconomyService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @AutoLog(value = "农村集体经济组织-批量删除")
    @ApiOperation(value="农村集体经济组织-批量删除", notes="农村集体经济组织-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.smartGroupEconomyService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartGroupEconomy smartGroupEconomy) {
        return super.exportXls(request, smartGroupEconomy, SmartGroupEconomy.class, "农村集体经济组织");
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SmartGroupEconomy.class);
    }
	/*---------------------------------主表处理-end-------------------------------------*/
	

    /*--------------------------------子表处理-集体经济组织人员-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	@AutoLog(value = "集体经济组织人员-通过主表ID查询")
	@ApiOperation(value="集体经济组织人员-通过主表ID查询", notes="集体经济组织人员-通过主表ID查询")
	@GetMapping(value = "/listSmartGroupEconomyPeopleByMainId")
    public Result<?> listSmartGroupEconomyPeopleByMainId(SmartGroupEconomyPeople smartGroupEconomyPeople,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<SmartGroupEconomyPeople> queryWrapper = QueryGenerator.initQueryWrapper(smartGroupEconomyPeople, req.getParameterMap());
        Page<SmartGroupEconomyPeople> page = new Page<SmartGroupEconomyPeople>(pageNo, pageSize);
        IPage<SmartGroupEconomyPeople> pageList = smartGroupEconomyPeopleService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 添加
	 * @param smartGroupEconomyPeople
	 * @return
	 */
	@AutoLog(value = "集体经济组织人员-添加")
	@ApiOperation(value="集体经济组织人员-添加", notes="集体经济组织人员-添加")
	@PostMapping(value = "/addSmartGroupEconomyPeople")
	public Result<?> addSmartGroupEconomyPeople(@RequestBody SmartGroupEconomyPeople smartGroupEconomyPeople) {

		smartGroupEconomyPeopleService.save(smartGroupEconomyPeople);

		ImageUtils imageUtils = new ImageUtils();

		FaceRecognitionUtil faceRecognitionUtil = new FaceRecognitionUtil();

		String imgPath = smartGroupEconomyPeople.getPic();

		String imgBase64 = imageUtils.getBase64ByImgUrl(UrlUtil.urlEncodeChinese( fileBaseUrl + imgPath));

		try {

			faceRecognitionUtil.createUserGroup(groupId);

			JSONObject faceResponse = faceRecognitionUtil.registerFace(imgBase64, groupId,
					smartGroupEconomyPeople.getId());

			log.info(String.valueOf(faceResponse));

			if(faceResponse.getIntValue("error_code") != 0) {
				smartGroupEconomyPeopleService.removeById(smartGroupEconomyPeople.getId());
				return Result.error(faceResponse.getString("error_msg"));
			} else {
				smartGroupEconomyPeople.setFaceToken(faceResponse.getJSONObject("result").getString("face_token"));
				smartGroupEconomyPeopleService.updateById(smartGroupEconomyPeople);
				return Result.OK("添加成功！");
			}

		} catch (RuntimeException e) {
			return Result.error(e.getMessage());
		}
	}

    /**
	 * 编辑
	 * @param smartGroupEconomyPeople
	 * @return
	 */
	@AutoLog(value = "集体经济组织人员-编辑")
	@ApiOperation(value="集体经济组织人员-编辑", notes="集体经济组织人员-编辑")
	@PutMapping(value = "/editSmartGroupEconomyPeople")
	public Result<?> editSmartGroupEconomyPeople(@RequestBody SmartGroupEconomyPeople smartGroupEconomyPeople) {
		ImageUtils imageUtils = new ImageUtils();

		FaceRecognitionUtil faceRecognitionUtil = new FaceRecognitionUtil();

		String imgPath = smartGroupEconomyPeople.getPic();

		String imgBase64 = imageUtils.getBase64ByImgUrl(UrlUtil.urlEncodeChinese( fileBaseUrl + imgPath));

		try {
			JSONObject faceResponse = faceRecognitionUtil.updateFace(imgBase64, groupId,
					smartGroupEconomyPeople.getId());

			log.info(String.valueOf(faceResponse));

			if(faceResponse.getIntValue("error_code") != 0) {
				return Result.error(faceResponse.getString("error_msg"));
			} else {
				smartGroupEconomyPeople.setFaceToken(faceResponse.getJSONObject("result").getString("face_token"));
				smartGroupEconomyPeopleService.updateById(smartGroupEconomyPeople);
				return Result.OK("编辑成功！");
			}

		} catch (RuntimeException e) {
			return Result.error(e.getMessage());
		}
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "集体经济组织人员-通过id删除")
	@ApiOperation(value="集体经济组织人员-通过id删除", notes="集体经济组织人员-通过id删除")
	@DeleteMapping(value = "/deleteSmartGroupEconomyPeople")
	public Result<?> deleteSmartGroupEconomyPeople(@RequestParam(name="id",required=true) String id) {
		FaceRecognitionUtil faceRecognitionUtil = new FaceRecognitionUtil();

		SmartGroupEconomyPeople smartGroupEconomyPeople = smartGroupEconomyPeopleService.getById(id);

		try {
			JSONObject deleteResponse = faceRecognitionUtil.deleteUser(groupId, smartGroupEconomyPeople.getId(),
					smartGroupEconomyPeople.getFaceToken());
			if(deleteResponse.getIntValue("error_code") != 0) {
				return Result.error(deleteResponse.getString("error_msg"));
			} else {
				smartGroupEconomyPeopleService.removeById(id);
				return Result.ok("删除成功");
			}
		} catch (RuntimeException e) {
			return Result.error(e.getMessage());
		}
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "集体经济组织人员-批量删除")
	@ApiOperation(value="集体经济组织人员-批量删除", notes="集体经济组织人员-批量删除")
	@DeleteMapping(value = "/deleteBatchSmartGroupEconomyPeople")
	public Result<?> deleteBatchSmartGroupEconomyPeople(@RequestParam(name="ids",required=true) String ids) {
	    this.smartGroupEconomyPeopleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportSmartGroupEconomyPeople")
    public ModelAndView exportSmartGroupEconomyPeople(HttpServletRequest request, SmartGroupEconomyPeople smartGroupEconomyPeople) {
		 // Step.1 组装查询条件
		 QueryWrapper<SmartGroupEconomyPeople> queryWrapper = QueryGenerator.initQueryWrapper(smartGroupEconomyPeople, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<SmartGroupEconomyPeople> pageList = smartGroupEconomyPeopleService.list(queryWrapper);
		 List<SmartGroupEconomyPeople> exportList = null;

		 // 过滤选中数据
		 String selections = request.getParameter("selections");
		 if (oConvertUtils.isNotEmpty(selections)) {
			 List<String> selectionList = Arrays.asList(selections.split(","));
			 exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		 } else {
			 exportList = pageList;
		 }

		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, "集体经济组织人员"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.CLASS, SmartGroupEconomyPeople.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("集体经济组织人员报表", "导出人:" + sysUser.getRealname(), "集体经济组织人员"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importSmartGroupEconomyPeople/{mainId}")
    public Result<?> importSmartGroupEconomyPeople(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			 MultipartFile file = entity.getValue();// 获取上传文件对象
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<SmartGroupEconomyPeople> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartGroupEconomyPeople.class, params);
				 for (SmartGroupEconomyPeople temp : list) {
                    temp.setMainId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 smartGroupEconomyPeopleService.saveBatch(list);
				 log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
				 return Result.OK("文件导入成功！数据行数：" + list.size());
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
		 return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-集体经济组织人员-end----------------------------------------------*/

    /*--------------------------------子表处理-集体经济组织会议-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	@AutoLog(value = "集体经济组织会议-通过主表ID查询")
	@ApiOperation(value="集体经济组织会议-通过主表ID查询", notes="集体经济组织会议-通过主表ID查询")
	@GetMapping(value = "/listSmartGroupEconomyMeetingByMainId")
    public Result<?> listSmartGroupEconomyMeetingByMainId(SmartGroupEconomyMeeting smartGroupEconomyMeeting,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<SmartGroupEconomyMeeting> queryWrapper = QueryGenerator.initQueryWrapper(smartGroupEconomyMeeting, req.getParameterMap());
        Page<SmartGroupEconomyMeeting> page = new Page<SmartGroupEconomyMeeting>(pageNo, pageSize);
        IPage<SmartGroupEconomyMeeting> pageList = smartGroupEconomyMeetingService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 添加
	 * @param smartGroupEconomyMeeting
	 * @return
	 */
	@AutoLog(value = "集体经济组织会议-添加")
	@ApiOperation(value="集体经济组织会议-添加", notes="集体经济组织会议-添加")
	@PostMapping(value = "/addSmartGroupEconomyMeeting")
	public Result<?> addSmartGroupEconomyMeeting(@RequestBody SmartGroupEconomyMeeting smartGroupEconomyMeeting) {
		smartGroupEconomyMeetingService.save(smartGroupEconomyMeeting);
		SmartGroupEconomy smartGroupEconomy = smartGroupEconomyService.getById(smartGroupEconomyMeeting.getMainId());
		smartGroupEconomy.setMeetingStatus("1");
		smartGroupEconomyService.updateById(smartGroupEconomy);
		return Result.OK("添加成功！");
	}

	 /**
	  * 通过主表id查询会议
	  *
	  * @param mainId
	  * @return
	  */
	 @AutoLog(value = "集体经济组织会议-通过mainId查询")
	 @ApiOperation(value="集体经济组织会议-通过mainId查询", notes="集体经济组织会议-通过mainId查询")
	 @GetMapping(value = "/getMeetingByMainId")
	 public Result<?> queryById(@RequestParam(name="mainId",required=true) String mainId) {
		 QueryWrapper<SmartGroupEconomyMeeting> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("main_id",mainId);
		 SmartGroupEconomyMeeting smartGroupEconomyMeeting = smartGroupEconomyMeetingService.getOne(queryWrapper);
		 if(smartGroupEconomyMeeting==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.OK(smartGroupEconomyMeeting);
	 }

    /**
	 * 编辑
	 * @param smartGroupEconomyMeeting
	 * @return
	 */
	@AutoLog(value = "集体经济组织会议-编辑")
	@ApiOperation(value="集体经济组织会议-编辑", notes="集体经济组织会议-编辑")
	@PutMapping(value = "/editSmartGroupEconomyMeeting")
	public Result<?> editSmartGroupEconomyMeeting(@RequestBody SmartGroupEconomyMeeting smartGroupEconomyMeeting) {
		smartGroupEconomyMeetingService.updateById(smartGroupEconomyMeeting);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "集体经济组织会议-通过id删除")
	@ApiOperation(value="集体经济组织会议-通过id删除", notes="集体经济组织会议-通过id删除")
	@DeleteMapping(value = "/deleteSmartGroupEconomyMeeting")
	public Result<?> deleteSmartGroupEconomyMeeting(@RequestParam(name="id",required=true) String id) {
		smartGroupEconomyMeetingService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "集体经济组织会议-批量删除")
	@ApiOperation(value="集体经济组织会议-批量删除", notes="集体经济组织会议-批量删除")
	@DeleteMapping(value = "/deleteBatchSmartGroupEconomyMeeting")
	public Result<?> deleteBatchSmartGroupEconomyMeeting(@RequestParam(name="ids",required=true) String ids) {
	    this.smartGroupEconomyMeetingService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportSmartGroupEconomyMeeting")
    public ModelAndView exportSmartGroupEconomyMeeting(HttpServletRequest request, SmartGroupEconomyMeeting smartGroupEconomyMeeting) {
		 // Step.1 组装查询条件
		 QueryWrapper<SmartGroupEconomyMeeting> queryWrapper = QueryGenerator.initQueryWrapper(smartGroupEconomyMeeting, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<SmartGroupEconomyMeeting> pageList = smartGroupEconomyMeetingService.list(queryWrapper);
		 List<SmartGroupEconomyMeeting> exportList = null;

		 // 过滤选中数据
		 String selections = request.getParameter("selections");
		 if (oConvertUtils.isNotEmpty(selections)) {
			 List<String> selectionList = Arrays.asList(selections.split(","));
			 exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		 } else {
			 exportList = pageList;
		 }

		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, "集体经济组织会议"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.CLASS, SmartGroupEconomyMeeting.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("集体经济组织会议报表", "导出人:" + sysUser.getRealname(), "集体经济组织会议"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importSmartGroupEconomyMeeting/{mainId}")
    public Result<?> importSmartGroupEconomyMeeting(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			 MultipartFile file = entity.getValue();// 获取上传文件对象
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<SmartGroupEconomyMeeting> list = ExcelImportUtil.importExcel(file.getInputStream(), SmartGroupEconomyMeeting.class, params);
				 for (SmartGroupEconomyMeeting temp : list) {
                    temp.setMainId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 smartGroupEconomyMeetingService.saveBatch(list);
				 log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
				 return Result.OK("文件导入成功！数据行数：" + list.size());
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
		 return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-集体经济组织会议-end----------------------------------------------*/




}
