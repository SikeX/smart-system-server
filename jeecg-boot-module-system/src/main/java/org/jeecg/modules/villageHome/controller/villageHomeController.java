package org.jeecg.modules.villageHome.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.villageHome.entity.villageHome;
import org.jeecg.modules.villageHome.service.IvillageHomeService;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 乡镇户口表
 * @Author: jeecg-boot
 * @Date:   2021-12-24
 * @Version: V1.0
 */
@Api(tags="乡镇户口表")
@RestController
@RequestMapping("/villageHome/villageHome")
@Slf4j
public class villageHomeController extends JeecgController<villageHome, IvillageHomeService> {
	@Autowired
	private IvillageHomeService villageHomeService;

	 @Autowired
	 private ISysUserService sysUserService;

	 @Autowired
	 private ISysDepartService sysDepartService;


	 /**
	 * 分页列表查询
	 *
	 * @param villageHome
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "乡镇户口表-分页列表查询")
	@ApiOperation(value="乡镇户口表-分页列表查询", notes="乡镇户口表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(villageHome villageHome,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<villageHome> queryWrapper = QueryGenerator.initQueryWrapper(villageHome, req.getParameterMap());
//		List<villageHome> villageHomeList = villageHomeService.list(queryWrapper);
		Page<villageHome> page = new Page<villageHome>(pageNo, pageSize);
		IPage<villageHome> pageList = villageHomeService.page(page, queryWrapper);
		List<villageHome> villageHomeList = pageList.getRecords();
		for (int i = 0; i < villageHomeList.size(); i++)
		{
			villageHome home = villageHomeList.get(i);
			SysUser host = sysUserService.getById(home.getHostId());
			home.setPhone(host.getPhone());
			home.setRealname(host.getRealname());
			home.setRole(host.getRole());
			List<SysUser> userList = sysUserService.queryByHomeCode(home.getHomeCode());
			home.setUserList(userList);
			villageHomeList.set(i,home);
		}
		pageList.setRecords(villageHomeList);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param villageHome
	 * @return
	 */
	@AutoLog(value = "乡镇户口表-添加")
	@ApiOperation(value="乡镇户口表-添加", notes="乡镇户口表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody villageHome villageHome) {
		Result<?> result = new Result<>();
		//判断该户主是否已经有户籍关系
		SysUser host = sysUserService.getById(villageHome.getHostId());
		if(host.getHomeCode()!=null && !host.getHomeCode().equals(""))
		{
			result.setSuccess(false);
			result.setMessage("所选择户主已有户籍信息！");
			return result;
		}
		if(host.getPhone()==null || host.getPhone().equals(""))
		{
			result.setSuccess(false);
			result.setMessage("户主电话号不可为空！");
			return result;
		}
		//判断家庭成员是否已经有户籍关系
		List<SysUser> userList = villageHome.getUserList();
		if(!userList.isEmpty())
		{
			for(SysUser user:userList)
			{
				if(user.getId()!=null && !user.getId().equals(""))
				{
					SysUser dbUser = sysUserService.getById(user.getId());
					if(dbUser.getHomeCode()!=null && !dbUser.getHomeCode().equals(""))
					{
						result.setSuccess(false);
						result.setMessage("所选择家庭成员已有户籍信息！");
						return result;
					}
				}

			}
		}
		//户主：户口本编号+户籍角色
		String homeCode = villageHome.getHomeCode();
		host.setHomeCode(homeCode);
		host.setHomeRole(1);
		sysUserService.updateById(host);
		//家庭成员：户口本编号+户籍角色
		if(!userList.isEmpty())
		{
			for(SysUser user:userList)
			{
					if (user.getId() != null && !user.getId().equals("")) {
						user.setHomeCode(homeCode);
						user.setHomeRole(2);
						sysUserService.updateById(user);
					}
			}
		}
        //添加户籍信息
		villageHome.setUserList(null);
		villageHome.setRealname(null);
		villageHome.setPhone(null);
		villageHome.setRole(null);
		villageHomeService.save(villageHome);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param villageHome
	 * @return
	 */
	@AutoLog(value = "乡镇户口表-编辑")
	@ApiOperation(value="乡镇户口表-编辑", notes="乡镇户口表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody villageHome villageHome) {
		Result<?> result = new Result<>();
		//获取编辑前的户籍信息
		villageHome bVillageHome = villageHomeService.getById(villageHome.getId());
		//获取编辑前的户口本编号
		String bHomeCode = bVillageHome.getHomeCode();
		//获取编辑后的户口本编号
		String lHomeCode = villageHome.getHomeCode();
		//获取编辑前的户主ID
		String bHostId = bVillageHome.getHostId();
		//获取编辑后的户主ID
		String lHostId = villageHome.getHostId();
		//获取编辑前的成员信息
		List<SysUser> bUserList = sysUserService.queryByHomeCode(bHomeCode);
		//获取编辑后的成员信息
		List<SysUser> lUserList = villageHome.getUserList();
		//判断更换后的户主是否已经有户籍关系
		if(!bHostId.equals(lHostId))
		{
			SysUser lHost = sysUserService.getById(lHostId);
			if(lHost.getHomeCode() != null && !lHost.getHomeCode().equals(""))
			{
				result.setSuccess(false);
				result.setMessage("更换的户主已有户籍信息！");
				return result;
			}
			if(lHost.getPhone()==null || lHost.getPhone().equals(""))
			{
				result.setSuccess(false);
				result.setMessage("户主电话号不可为空！");
				return result;
			}

		}
		//判断家庭成员是否已有户籍关系
		if(!lUserList.isEmpty())
		{
			//新增的成员
			for(SysUser luser:lUserList)

			{
				if(luser.getId()!=null && !luser.getId().equals("")) {
					String lUserId = luser.getId();
					int flag = 0;
					for (SysUser buser : bUserList) {
						String bUserId = buser.getId();
						if (bUserId.equals(lUserId)) {
							flag = 1;
						}
					}
					if (flag == 0) {
						SysUser dbUser = sysUserService.getById(luser.getId());
						if(dbUser.getHomeCode()!=null && !dbUser.getHomeCode().equals(""))
						{
							result.setSuccess(false);
							result.setMessage("新增家庭成员已有户籍信息！");
							return result;
						}
					}
				}
			}
		}
        //保存编辑后的户主信息（假设户口本编号未更改，编辑后的户主保存原户口本编号信息）
		 if(!bHostId.equals(lHostId))
		 {
		 	SysUser laterHost = sysUserService.getById(lHostId);
		 	SysUser bHost = sysUserService.getById(bHostId);

		 	bHost.setHomeCode("");
		 	bHost.setHomeRole(0);
		 	sysUserService.updateById(bHost);
		 	laterHost.setHomeCode(bHomeCode);
		 	laterHost.setHomeRole(1);
		 	sysUserService.updateById(laterHost);
		 }
        //保存编辑后的成员信息(假设户口本编号未更改，编辑后的成员字段中保存原户口本编号信息)
		//新增的成员
		for(SysUser luser:lUserList)

		{
			if(luser.getId()!=null && !luser.getId().equals("")) {
				String lUserId = luser.getId();
				int flag = 0;
				for (SysUser buser : bUserList) {
					String bUserId = buser.getId();
					if (bUserId.equals(lUserId)) {
						flag = 1;
					}
				}
				if (flag == 0) {
					luser.setHomeCode(bHomeCode);
					luser.setHomeRole(2);
					sysUserService.updateById(luser);
				}
			}
		}
		//删去的成员
		for(SysUser buser:bUserList)
		{
			String bUserId = buser.getId();
			int flag = 0;
			for(SysUser luser: lUserList )
			{
				if(luser.getId()!=null || !luser.getId().equals("")){
				String lUserId = luser.getId();
				if(lUserId.equals(bUserId))
				{
					flag = 1;
				}}
			}
			if(flag == 0)
			{
				buser.setHomeCode("");
				buser.setHomeRole(0);
				sysUserService.updateById(buser);
			}
		}
        //保存编辑后的户口信息,如果户口本编号变动，更新户主和家庭成员的home_code
		if(!lHomeCode.equals(bHomeCode))
		{
			SysUser host = sysUserService.getById(lHostId);
			host.setHomeCode(lHomeCode);
			sysUserService.updateById(host);
			for(SysUser user:lUserList)
			{
				if(user.getId()!=null && !user.getId().equals(""))
				{user.setHomeCode(lHomeCode);
				user.setHomeCode(lHomeCode);
				sysUserService.updateById(user);}
			}
		}
		villageHome.setUserList(null);
		villageHome.setRealname(null);
		villageHome.setPhone(null);
		villageHome.setRole(null);
		villageHomeService.updateById(villageHome);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "乡镇户口表-通过id删除")
	@ApiOperation(value="乡镇户口表-通过id删除", notes="乡镇户口表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		villageHome villageHome = villageHomeService.getById(id);
		SysUser host = sysUserService.getById(villageHome.getHostId());
		host.setHomeCode("");
		host.setHomeRole(0);
		sysUserService.updateById(host);
		List<SysUser> userList = sysUserService.queryByHomeCode(villageHome.getHomeCode());
		for(SysUser user:userList)
		{
			user.setHomeCode("");
			user.setHomeRole(0);
			sysUserService.updateById(user);
		}
		villageHomeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "乡镇户口表-批量删除")
	@ApiOperation(value="乡镇户口表-批量删除", notes="乡镇户口表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		List<String> villageHomeIds = Arrays.asList(ids.split(","));
		for(String id:villageHomeIds)
		{
			villageHome villageHome = villageHomeService.getById(id);
			SysUser host = sysUserService.getById(villageHome.getHostId());
			host.setHomeCode("");
			host.setHomeRole(0);
			sysUserService.updateById(host);
			List<SysUser> userList = sysUserService.queryByHomeCode(villageHome.getHomeCode());
			for(SysUser user:userList)
			{
				user.setHomeCode("");
				user.setHomeRole(0);
				sysUserService.updateById(user);
			}
		}
		this.villageHomeService.removeByIds(villageHomeIds);
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "乡镇户口表-通过id查询")
	@ApiOperation(value="乡镇户口表-通过id查询", notes="乡镇户口表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		villageHome villageHome = villageHomeService.getById(id);
		if(villageHome==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(villageHome);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param villageHome
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, villageHome villageHome) {
        return super.exportXls(request, villageHome, villageHome.class, "乡镇户口表");
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
        return super.importExcel(request, response, villageHome.class);
    }

}
