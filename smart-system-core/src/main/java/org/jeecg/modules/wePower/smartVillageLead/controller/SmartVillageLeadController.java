package org.jeecg.modules.wePower.smartVillageLead.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.VillageRelationModel;
import org.jeecg.modules.utils.FaceRecognitionUtil;
import org.jeecg.modules.utils.ImageUtils;
import org.jeecg.modules.utils.UrlUtil;
import org.jeecg.modules.wePower.smartEvadeRelation.entity.SmartEvadeRelation;
import org.jeecg.modules.wePower.smartEvadeRelation.service.ISmartEvadeRelationService;
import org.jeecg.modules.wePower.smartPublicityResource.entity.SmartPublicityResource;
import org.jeecg.modules.wePower.smartVillageLead.entity.SmartVillageLead;
import org.jeecg.modules.wePower.smartVillageLead.service.ISmartVillageLeadService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 村（社区）领导班子
 * @Author: jeecg-boot
 * @Date:   2022-02-21
 * @Version: V1.0
 */
@Api(tags="村（社区）领导班子")
@RestController
@RequestMapping("/smartVillageLead/smartVillageLead")
@Slf4j
public class SmartVillageLeadController extends JeecgController<SmartVillageLead, ISmartVillageLeadService> {
	@Autowired
	private ISmartVillageLeadService smartVillageLeadService;
	@Autowired
	private ISysBaseAPI sysBaseAPI;
	@Autowired
	private ISmartEvadeRelationService smartEvadeRelationService;

	@Value("${jeecg.fileBaseUrl}")
	private String fileBaseUrl;

	private String groupId = "villageLead";
	
	/**
	 * 分页列表查询
	 *
	 * @param smartVillageLead
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "村（社区）领导班子-分页列表查询")
	@ApiOperation(value="村（社区）领导班子-分页列表查询", notes="村（社区）领导班子-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartVillageLead smartVillageLead,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartVillageLead> queryWrapper = QueryGenerator.initQueryWrapper(smartVillageLead, req.getParameterMap());
		Page<SmartVillageLead> page = new Page<SmartVillageLead>(pageNo, pageSize);
		IPage<SmartVillageLead> pageList = smartVillageLeadService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @AutoLog(value = "农村集体经济组织-分页列表查询")
	 @ApiOperation(value="农村集体经济组织-分页列表查询", notes="农村集体经济组织-分页列表查询")
	 @GetMapping(value = "/listAdmin")
	 public Result<?> queryPageListAdmin(SmartVillageLead smartVillageLead,
										 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										 HttpServletRequest req) {

		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String orgCode = sysUser.getOrgCode();
		 if ("".equals(orgCode)) {
			 return Result.error("本用户没有操作权限！");
		 }
		 QueryWrapper<SmartVillageLead> queryWrapper = QueryGenerator.initQueryWrapper(smartVillageLead, req.getParameterMap());
		 queryWrapper.eq("sys_org_code", orgCode);
		 Page<SmartVillageLead> page = new Page<SmartVillageLead>(pageNo, pageSize);
		 IPage<SmartVillageLead> pageList = smartVillageLeadService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }
	
	/**
	 *   添加
	 *
	 * @param smartVillageLead
	 * @return
	 */
	@AutoLog(value = "村（社区）领导班子-添加")
	@ApiOperation(value="村（社区）领导班子-添加", notes="村（社区）领导班子-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartVillageLead smartVillageLead) throws UnsupportedEncodingException {


		smartVillageLeadService.save(smartVillageLead);

		LoginUser user = sysBaseAPI.getUserByIdNumber(smartVillageLead.getPeople());

		if(user.getHomeRole().equals("1")){
			SmartEvadeRelation relation = new SmartEvadeRelation();
			relation.setHostName(smartVillageLead.getPeople());
		} else if (user.getHomeRole().equals("2")){
			List<VillageRelationModel> relationList = sysBaseAPI.getVillageRelation(smartVillageLead.getPeople(), "2");
//			List<String> hostList = new ArrayList<>();
//			relationList.forEach(relation -> {
//				hostList.add(relation.get);
//			});
			smartEvadeRelationService.list().forEach(relation -> {
				relationList.forEach(relationModel -> {
					if(relation.getHostName().equals(relationModel.getHostIdnumber())){
						relation.setRelation(relationModel.getHomeRelation().toString());
						smartEvadeRelationService.updateById(relation);
					}
				});
			});
		}

		ImageUtils imageUtils = new ImageUtils();

		FaceRecognitionUtil faceRecognitionUtil = new FaceRecognitionUtil();

		String imgPath = smartVillageLead.getPicture();

		String imgBase64 = imageUtils.getBase64ByImgUrl(UrlUtil.urlEncodeChinese( fileBaseUrl + imgPath));

		try {

			faceRecognitionUtil.createUserGroup(groupId);

			JSONObject faceResponse = faceRecognitionUtil.registerFace(imgBase64, groupId, smartVillageLead.getId());

			log.info(String.valueOf(faceResponse));

			if(faceResponse.getIntValue("error_code") != 0) {
				smartVillageLeadService.removeById(smartVillageLead.getId());
				return Result.error(faceResponse.getString("error_msg"));
			} else {
				smartVillageLead.setFaceToken(faceResponse.getJSONObject("result").getString("face_token"));
				smartVillageLeadService.updateById(smartVillageLead);
				return Result.OK("添加成功！");
			}

		} catch (RuntimeException e) {
			return Result.error(e.getMessage());
		}
	}
	
	/**
	 *  编辑
	 *
	 * @param smartVillageLead
	 * @return
	 */
	@AutoLog(value = "村（社区）领导班子-编辑")
	@ApiOperation(value="村（社区）领导班子-编辑", notes="村（社区）领导班子-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartVillageLead smartVillageLead) {

		String name = sysBaseAPI.translateDictFromTable("smart_village_home","home_surname", "idnumber",
				smartVillageLead.getPeople());

		smartVillageLead.setName(name);

		ImageUtils imageUtils = new ImageUtils();

		FaceRecognitionUtil faceRecognitionUtil = new FaceRecognitionUtil();

		String imgPath = smartVillageLead.getPicture();

		String imgBase64 = imageUtils.getBase64ByImgUrl(UrlUtil.urlEncodeChinese( fileBaseUrl + imgPath));

		try {
			JSONObject faceResponse = faceRecognitionUtil.updateFace(imgBase64, groupId, smartVillageLead.getId());

			log.info(String.valueOf(faceResponse));

			if(faceResponse.getIntValue("error_code") != 0) {
				return Result.error(faceResponse.getString("error_msg"));
			} else {
				smartVillageLead.setFaceToken(faceResponse.getJSONObject("result").getString("face_token"));
				smartVillageLeadService.updateById(smartVillageLead);
				return Result.OK("编辑成功！");
			}

		} catch (RuntimeException e) {
			return Result.error(e.getMessage());
		}
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "村（社区）领导班子-通过id删除")
	@ApiOperation(value="村（社区）领导班子-通过id删除", notes="村（社区）领导班子-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		FaceRecognitionUtil faceRecognitionUtil = new FaceRecognitionUtil();

		SmartVillageLead smartVillageLead = smartVillageLeadService.getById(id);

		try {
			JSONObject deleteResponse = faceRecognitionUtil.deleteUser(groupId, smartVillageLead.getId(),
					smartVillageLead.getFaceToken());
			if(deleteResponse.getIntValue("error_code") != 0) {
				return Result.error(deleteResponse.getString("error_msg"));
			} else {
				smartVillageLeadService.removeById(id);
				return Result.ok("删除成功");
			}
		} catch (RuntimeException e) {
			return Result.error(e.getMessage());
		}
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "村（社区）领导班子-批量删除")
	@ApiOperation(value="村（社区）领导班子-批量删除", notes="村（社区）领导班子-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartVillageLeadService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "村（社区）领导班子-通过id查询")
	@ApiOperation(value="村（社区）领导班子-通过id查询", notes="村（社区）领导班子-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartVillageLead smartVillageLead = smartVillageLeadService.getById(id);
		if(smartVillageLead==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartVillageLead);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartVillageLead
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartVillageLead smartVillageLead) {
        return super.exportXls(request, smartVillageLead, SmartVillageLead.class, "村（社区）领导班子");
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
        return super.importExcel(request, response, SmartVillageLead.class);
    }

}
