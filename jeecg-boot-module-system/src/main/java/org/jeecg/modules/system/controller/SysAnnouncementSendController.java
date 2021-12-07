package org.jeecg.modules.system.controller;

import java.util.*;
import org.jeecg.common.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysAnnouncement;
import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.model.AnnouncementSendModel;
import org.jeecg.modules.system.model.TaskManageModel;
import org.jeecg.modules.system.service.ISysAnnouncementSendService;
import org.jeecg.modules.system.service.ISysAnnouncementService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

 /**
 * @Title: Controller
 * @Description: 用户通告阅读标记表
 * @Author: jeecg-boot
 * @Date:  2019-02-21
 * @Version: V1.0
 */
@RestController
@RequestMapping("/sys/sysAnnouncementSend")
@Slf4j
public class SysAnnouncementSendController {
	@Autowired
	private ISysAnnouncementSendService sysAnnouncementSendService;
	@Autowired
	private ISysAnnouncementService sysAnnouncementService;
	@Autowired
	private ISysBaseAPI sysBaseAPI;
	@Autowired
	private ISysUserService sysUserService;
	
	/**
	  * 分页列表查询
	 * @param sysAnnouncementSend
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<SysAnnouncementSend>> queryPageList(SysAnnouncementSend sysAnnouncementSend,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<SysAnnouncementSend>> result = new Result<IPage<SysAnnouncementSend>>();
		QueryWrapper<SysAnnouncementSend> queryWrapper = new QueryWrapper<SysAnnouncementSend>(sysAnnouncementSend);
		Page<SysAnnouncementSend> page = new Page<SysAnnouncementSend>(pageNo,pageSize);
		//排序逻辑 处理
		String column = req.getParameter("column");
		String order = req.getParameter("order");
		if(oConvertUtils.isNotEmpty(column) && oConvertUtils.isNotEmpty(order)) {
			if("asc".equals(order)) {
				queryWrapper.orderByAsc(oConvertUtils.camelToUnderline(column));
			}else {
				queryWrapper.orderByDesc(oConvertUtils.camelToUnderline(column));
			}
		}
		IPage<SysAnnouncementSend> pageList = sysAnnouncementSendService.page(page, queryWrapper);
		//log.info("查询当前页："+pageList.getCurrent());
		//log.info("查询当前页数量："+pageList.getSize());
		//log.info("查询结果数量："+pageList.getRecords().size());
		//log.info("数据总数："+pageList.getTotal());
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param sysAnnouncementSend
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<SysAnnouncementSend> add(@RequestBody SysAnnouncementSend sysAnnouncementSend) {
		Result<SysAnnouncementSend> result = new Result<SysAnnouncementSend>();
		try {
			sysAnnouncementSendService.save(sysAnnouncementSend);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param sysAnnouncementSend
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<SysAnnouncementSend> edit(@RequestBody SysAnnouncementSend sysAnnouncementSend) {
		Result<SysAnnouncementSend> result = new Result<SysAnnouncementSend>();
		SysAnnouncementSend sysAnnouncementSendEntity = sysAnnouncementSendService.getById(sysAnnouncementSend.getId());
		if(sysAnnouncementSendEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysAnnouncementSendService.updateById(sysAnnouncementSend);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<SysAnnouncementSend> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysAnnouncementSend> result = new Result<SysAnnouncementSend>();
		SysAnnouncementSend sysAnnouncementSend = sysAnnouncementSendService.getById(id);
		if(sysAnnouncementSend==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysAnnouncementSendService.removeById(id);
			if(ok) {
				result.success("删除成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<SysAnnouncementSend> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysAnnouncementSend> result = new Result<SysAnnouncementSend>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.sysAnnouncementSendService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<SysAnnouncementSend> queryById(@RequestParam(name="id",required=true) String id) {
		Result<SysAnnouncementSend> result = new Result<SysAnnouncementSend>();
		SysAnnouncementSend sysAnnouncementSend = sysAnnouncementSendService.getById(id);
		if(sysAnnouncementSend==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(sysAnnouncementSend);
			result.setSuccess(true);
		}
		return result;
	}
	
	/**
	 * @功能：更新用户系统消息阅读状态
	 * @param json
	 * @return
	 */
	@PutMapping(value = "/editByAnntIdAndUserId")
	public Result<SysAnnouncementSend> editById(@RequestBody JSONObject json) {
		Result<SysAnnouncementSend> result = new Result<SysAnnouncementSend>();
		String anntId = json.getString("anntId");
		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		String userId = sysUser.getId();
		QueryWrapper<SysAnnouncementSend> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("annt_id",anntId);
		queryWrapper.eq("user_id",userId);

		String readFlag = sysAnnouncementSendService.getOne(queryWrapper).getReadFlag();
		if(Objects.equals(readFlag, "0")){
			Integer readCount = sysAnnouncementService.getById(anntId).getReadCount() + 1;
			log.info("here is:"+readCount);
			SysAnnouncement sysAnnouncement = new SysAnnouncement();
			sysAnnouncement.setId(anntId);
			sysAnnouncement.setReadCount(readCount);
			sysAnnouncementService.updateById(sysAnnouncement);
		}

		LambdaUpdateWrapper<SysAnnouncementSend> updateWrapper = new UpdateWrapper().lambda();
		updateWrapper.set(SysAnnouncementSend::getReadFlag, CommonConstant.HAS_READ_FLAG);
		// 增加是否超时状态
		Date readTime = new Date();
		Date endTime = sysAnnouncementService.getById(anntId).getEndTime();
		Integer isDelay;
		if(endTime != null){
			if(readTime.after(endTime)){
				isDelay = 1;
			} else {
				isDelay = 0;
			}
		} else {
			isDelay = 0;
		}
		updateWrapper.set(SysAnnouncementSend::getReadTime, readTime);
		updateWrapper.set(SysAnnouncementSend::getIsDelay, isDelay);
		updateWrapper.last("where annt_id ='"+anntId+"' and user_id ='"+userId+"'");
		SysAnnouncementSend announcementSend = new SysAnnouncementSend();
		sysAnnouncementSendService.update(announcementSend, updateWrapper);
		result.setSuccess(true);
		return result;
	}

	 /**
	  * @功能：更新用户提交任务状态
	  * @param json
	  * @return
	  */
	 @PutMapping(value = "/editTaskSubmit")
	 public Result<SysAnnouncementSend> editById(@RequestBody SysAnnouncementSend sysAnnouncementSend) {
		 Result<SysAnnouncementSend> result = new Result<SysAnnouncementSend>();
		 String anntId = sysAnnouncementSend.getAnntId();
		 QueryWrapper<SysAnnouncementSend> queryWrapper = new QueryWrapper<>();
		 LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		 String userId = sysUser.getId();
		 queryWrapper.eq("annt_id",anntId).eq("user_id",userId);
		 sysAnnouncementSendService.update(sysAnnouncementSend, queryWrapper);
		 result.setSuccess(true);
		 return result;
	 }
	
	/**
	 * @功能：获取我的消息
	 * @return
	 */
	@GetMapping(value = "/getMyAnnouncementSend")
	public Result<IPage<AnnouncementSendModel>> getMyAnnouncementSend(AnnouncementSendModel announcementSendModel,
			@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		String type = "msg";
		Result<IPage<AnnouncementSendModel>> result = new Result<IPage<AnnouncementSendModel>>();
		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		String userId = sysUser.getId();
		announcementSendModel.setUserId(userId);
		announcementSendModel.setPageNo((pageNo-1)*pageSize);
		announcementSendModel.setPageSize(pageSize);
		Page<AnnouncementSendModel> pageList = new Page<AnnouncementSendModel>(pageNo,pageSize);
		pageList = sysAnnouncementSendService.getMyAnnouncementSendPage(pageList, announcementSendModel, type);
		result.setResult(pageList);
		result.setSuccess(true);
		return result;
	}

	 @GetMapping(value = "/getMyAnnouncementSendMobile")
	 public Result<IPage<AnnouncementSendModel>> getMyAnnouncementSendMobile(AnnouncementSendModel announcementSendModel,
																	   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																	   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		 String type = "mobile";
		 Result<IPage<AnnouncementSendModel>> result = new Result<IPage<AnnouncementSendModel>>();
		 LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		 String userId = sysUser.getId();
		 announcementSendModel.setUserId(userId);
		 announcementSendModel.setPageNo((pageNo-1)*pageSize);
		 announcementSendModel.setPageSize(pageSize);
		 Page<AnnouncementSendModel> pageList = new Page<AnnouncementSendModel>(pageNo,pageSize);
		 pageList = sysAnnouncementSendService.getMyAnnouncementSendPage(pageList, announcementSendModel, type);
		 result.setResult(pageList);
		 result.setSuccess(true);
		 return result;
	 }

	 @GetMapping(value = "/getSubmitFileList")
	 public Result<List<String>> getSubmitFileList(@RequestParam(name="anntId") String anntId) {
		 Result<List<String>> result = new Result<List<String>>();

		 QueryWrapper<SysAnnouncementSend> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("annt_id",anntId);

		 List<String> fileList = new ArrayList<>();

		 List<SysAnnouncementSend> submitFileList = sysAnnouncementSendService.list(queryWrapper);

		 submitFileList.forEach(item->fileList.add(item.getSubmitFile()));

		 result.setResult(fileList);
		 result.setSuccess(true);
		 return result;
	 }

	 @GetMapping(value = "/getMyTaskSend")
	 public Result<IPage<AnnouncementSendModel>> getMyTaskSend(AnnouncementSendModel announcementSendModel,
																	   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																	   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		 Result<IPage<AnnouncementSendModel>> result = new Result<IPage<AnnouncementSendModel>>();
		 String type = "task";
		 LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		 String userId = sysUser.getId();
		 announcementSendModel.setUserId(userId);
		 announcementSendModel.setPageNo((pageNo-1)*pageSize);
		 announcementSendModel.setPageSize(pageSize);
		 Page<AnnouncementSendModel> pageList = new Page<AnnouncementSendModel>(pageNo,pageSize);
		 pageList = sysAnnouncementSendService.getMyAnnouncementSendPage(pageList, announcementSendModel,type);
		 result.setResult(pageList);
		 result.setSuccess(true);
		 return result;
	 }

	/**
	 * @功能：一键已读
	 * @return
	 */
	@PutMapping(value = "/readAll")
	public Result<SysAnnouncementSend> readAll() {
		Result<SysAnnouncementSend> result = new Result<SysAnnouncementSend>();
		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		String userId = sysUser.getId();
		LambdaUpdateWrapper<SysAnnouncementSend> updateWrapper = new UpdateWrapper().lambda();
		updateWrapper.set(SysAnnouncementSend::getReadFlag, CommonConstant.HAS_READ_FLAG);
		updateWrapper.set(SysAnnouncementSend::getReadTime, new Date());
		updateWrapper.last("where user_id ='"+userId+"'");
		SysAnnouncementSend announcementSend = new SysAnnouncementSend();
		sysAnnouncementSendService.update(announcementSend, updateWrapper);
		result.setSuccess(true);
		result.setMessage("全部已读");
		return result;
	}

	 /**
	  * 获取消息收发详情
	  *
	  * @param announcementSendModel
	  * @param anntId
	  * @param pageNo
	  * @param pageSize
	  * @return
	  */
	 @GetMapping(value = "/getTaskDetail")
	 public Result<IPage<AnnouncementSendModel>> getTaskDetail(AnnouncementSendModel announcementSendModel,
															 @RequestParam(name="anntId", required = true) String anntId,
															 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		 Result<IPage<AnnouncementSendModel>> result = new Result<IPage<AnnouncementSendModel>>();
//		 log.info(String.valueOf(announcementSendModel));
		 Page<AnnouncementSendModel> pageList = new Page<AnnouncementSendModel>(pageNo,pageSize);

		 pageList = sysAnnouncementSendService.getTaskSendPage(pageList, announcementSendModel);
		 result.setResult(pageList);
		 result.setSuccess(true);
		 return result;
	 }

	 @GetMapping(value = "/remindAll")
	 public Result<?> remindAll(SysAnnouncementSend sysAnnouncementSend,
								@RequestParam(name="anntId", required = true) String anntId,
								@RequestParam(name="type",required = false, defaultValue="【测试】") String type) {

		 LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();

		 QueryWrapper<SysAnnouncementSend> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("annt_id", anntId).eq("read_flag","0");


		 List<SysAnnouncementSend> unReadList = sysAnnouncementSendService.list(queryWrapper);

		 log.info("haha "+String.valueOf(unReadList));

		 List<String> unReadUserList = new ArrayList<>();
		 List<String> ubReadUserNameList = new ArrayList<>();
		 List<String> ubReadUserPhoneList = new ArrayList<>();

		 unReadList.forEach(item -> {
			 unReadUserList.add(item.getUserId());
		 });

		 unReadUserList.forEach(item -> {
			 ubReadUserNameList.add(sysUserService.getById(item).getRealname());
			 ubReadUserPhoneList.add(sysUserService.getById(item).getPhone());
		 });


		 String unReadUsers = String.join(",",unReadUserList);
		 String unReadUserName = String.join(",",ubReadUserNameList);
		 String unReadUserPhone = String.join(",",ubReadUserPhoneList);

		 MessageDTO messageDTO = new MessageDTO();

		 messageDTO.setFromUser(sysUser.getUsername());
		 messageDTO.setToUser(unReadUsers);
		 messageDTO.setCategory("1");
		 messageDTO.setTitle("您的"+type+"消息还未查收，请尽快查收");
		 messageDTO.setContent("您的"+type+"消息还未查收，请尽快查收");

		 sysBaseAPI.sendSysAnnouncementById(messageDTO);

		 // 发送短信提醒
		 String sendFrom = sysUser.getUsername();
		 String sendType = "test";  //发送类型，需求不明确，可以先不填
		 String tittle = "消息未读提醒";	//发送标题
		 String content = "您的"+type+"消息还未查收，请尽快查收";	//发送内容，请填写已报备的模板内容，带参数的模板，请把参数拼接好
		 String receiver = unReadUserName;  //接收人，真实姓名
		 String receiverPhone = unReadUserPhone;  //手机号，真实手机号
		 //发送给多人请用",”分隔接收人和手机号，例如
		 //String receiverPhone = "18989898989,19898989898";

		 //发送短信,核心代码
		 DySmsHelper.sendSms(sendFrom,sendType,tittle,content,receiver,receiverPhone);

		 return Result.OK("提醒成功");
	 }

}
