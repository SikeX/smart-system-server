package org.jeecg.modules.wePower.smartVillageLead2.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.utils.FaceRecognitionUtil;
import org.jeecg.modules.utils.ImageUtils;
import org.jeecg.modules.utils.UrlUtil;
import org.jeecg.modules.wePower.smartVillageLead.entity.SmartVillageLead;
import org.jeecg.modules.wePower.smartVillageLead2.entity.SmartVillageLead2;
import org.jeecg.modules.wePower.smartVillageLead2.service.ISmartVillageLead2Service;

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
 * @Description: 领导班子
 * @Author: jeecg-boot
 * @Date:   2022-03-09
 * @Version: V1.0
 */
@Api(tags="领导班子")
@RestController
@RequestMapping("/smartVillageLead2/smartVillageLead2")
@Slf4j
public class SmartVillageLead2Controller extends JeecgController<SmartVillageLead2, ISmartVillageLead2Service> {
	@Autowired
	private ISmartVillageLead2Service smartVillageLead2Service;

	@Value("${jeecg.fileBaseUrl}")
	private String fileBaseUrl;

	private String groupId = "villageLead2";

	 /**
	 * 分页列表查询
	 *
	 * @param smartVillageLead2
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "领导班子-分页列表查询")
	@ApiOperation(value="领导班子-分页列表查询", notes="领导班子-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SmartVillageLead2 smartVillageLead2,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SmartVillageLead2> queryWrapper = QueryGenerator.initQueryWrapper(smartVillageLead2, req.getParameterMap());
		Page<SmartVillageLead2> page = new Page<SmartVillageLead2>(pageNo, pageSize);
		IPage<SmartVillageLead2> pageList = smartVillageLead2Service.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @AutoLog(value = "农村集体经济组织-分页列表查询")
	 @ApiOperation(value="农村集体经济组织-分页列表查询", notes="农村集体经济组织-分页列表查询")
	 @GetMapping(value = "/listAdmin")
	 public Result<?> queryPageListAdmin(SmartVillageLead2 smartVillageLead,
										 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										 HttpServletRequest req) {

		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String orgCode = sysUser.getOrgCode();
		 if ("".equals(orgCode)) {
			 return Result.error("本用户没有操作权限！");
		 }
		 QueryWrapper<SmartVillageLead2> queryWrapper = QueryGenerator.initQueryWrapper(smartVillageLead, req.getParameterMap());
		 queryWrapper.eq("sys_org_code", orgCode);
		 Page<SmartVillageLead2> page = new Page<SmartVillageLead2>(pageNo, pageSize);
		 IPage<SmartVillageLead2> pageList = smartVillageLead2Service.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }
	
	/**
	 *   添加
	 *
	 * @param smartVillageLead2
	 * @return
	 */
	@AutoLog(value = "领导班子-添加")
	@ApiOperation(value="领导班子-添加", notes="领导班子-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SmartVillageLead2 smartVillageLead2) throws UnsupportedEncodingException {
		ImageUtils imageUtils = new ImageUtils();

		FaceRecognitionUtil faceRecognitionUtil = new FaceRecognitionUtil();

		smartVillageLead2Service.save(smartVillageLead2);

		String imgPath = smartVillageLead2.getPic();

		String imgBase64 = imageUtils.getBase64ByImgUrl(UrlUtil.urlEncodeChinese( imgPath));

		try {

			faceRecognitionUtil.createUserGroup(groupId);

			JSONObject faceResponse = faceRecognitionUtil.registerFace(imgBase64, groupId, smartVillageLead2.getId());

			log.info(String.valueOf(faceResponse));

			if(faceResponse.getIntValue("error_code") != 0) {
				return Result.error(faceResponse.getString("error_msg"));
			} else {
				smartVillageLead2.setFaceToken(faceResponse.getJSONObject("result").getString("face_token"));
				smartVillageLead2Service.updateById(smartVillageLead2);
				return Result.OK("添加成功！");
			}

		} catch (RuntimeException e) {
			return Result.error(e.getMessage());
		}
	}
	
	/**
	 *  编辑
	 *
	 * @param smartVillageLead2
	 * @return
	 */
	@AutoLog(value = "领导班子-编辑")
	@ApiOperation(value="领导班子-编辑", notes="领导班子-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SmartVillageLead2 smartVillageLead2) {

		ImageUtils imageUtils = new ImageUtils();

		FaceRecognitionUtil faceRecognitionUtil = new FaceRecognitionUtil();

		String imgPath = smartVillageLead2.getPic();

		String imgBase64 = imageUtils.getBase64ByImgUrl(UrlUtil.urlEncodeChinese( imgPath));

		try {
			JSONObject faceResponse = faceRecognitionUtil.updateFace(imgBase64, groupId, smartVillageLead2.getId());

			if(faceResponse.getIntValue("error_code") != 0) {
				return Result.error(faceResponse.getString("error_msg"));
			} else {
				smartVillageLead2.setFaceToken(faceResponse.getJSONObject("result").getString("face_token"));
				smartVillageLead2Service.updateById(smartVillageLead2);
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
	@AutoLog(value = "领导班子-通过id删除")
	@ApiOperation(value="领导班子-通过id删除", notes="领导班子-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {

		FaceRecognitionUtil faceRecognitionUtil = new FaceRecognitionUtil();

		SmartVillageLead2 smartVillageLead2 = smartVillageLead2Service.getById(id);

		try {
			JSONObject deleteResponse = faceRecognitionUtil.deleteUser(groupId, smartVillageLead2.getId(),
					smartVillageLead2.getFaceToken());
			if(deleteResponse.getIntValue("error_code") != 0) {
				return Result.error(deleteResponse.getString("error_msg"));
			} else {
				smartVillageLead2Service.removeById(id);
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
	@AutoLog(value = "领导班子-批量删除")
	@ApiOperation(value="领导班子-批量删除", notes="领导班子-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.smartVillageLead2Service.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "领导班子-通过id查询")
	@ApiOperation(value="领导班子-通过id查询", notes="领导班子-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SmartVillageLead2 smartVillageLead2 = smartVillageLead2Service.getById(id);
		if(smartVillageLead2==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(smartVillageLead2);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param smartVillageLead2
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartVillageLead2 smartVillageLead2) {
        return super.exportXls(request, smartVillageLead2, SmartVillageLead2.class, "领导班子");
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
        return super.importExcel(request, response, SmartVillageLead2.class);
    }

}
