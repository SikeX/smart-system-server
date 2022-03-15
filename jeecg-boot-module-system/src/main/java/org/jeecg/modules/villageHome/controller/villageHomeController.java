package org.jeecg.modules.villageHome.controller;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.common.util.ParamsUtil;
import org.jeecg.modules.smartFuneralReport.entity.SmartFuneralReport;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.villageHome.entity.villageHome;
import org.jeecg.modules.villageHome.vo.vHome;
import org.jeecg.modules.villageHome.entity.villageRelation;
import org.jeecg.modules.villageHome.service.IvillageHomeService;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.villageHome.service.IvillageRelationService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
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
	 private IvillageRelationService villageRelationService;

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
			if(home.getIdnumber()!=null && !home.getIdnumber().equals("")) {
				SysUser host = sysUserService.queryByIdnumber(home.getIdnumber());
				if(host!=null){
				if(host.getPhone()==null)
				{host.setPhone("");}
				home.setPhone(host.getPhone());
				home.setRealname(host.getRealname());
				home.setRole(host.getRole());}
			}
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
		SysUser host = sysUserService.queryByIdnumber(villageHome.getIdnumber());
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
						villageRelation relation = new villageRelation();
						relation.setHomeRelation(user.getRelation());
						relation.setIdnumber(user.getIdnumber());
						relation.setHomeCode(homeCode);
						relation.setHostIdnumber(host.getIdnumber());
						villageRelationService.save(relation);
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
		//获取编辑前的户主身份证号
		String bHostId = bVillageHome.getIdnumber();
		//获取编辑后的户主身份证号
		String lHostId = villageHome.getIdnumber();
		//获取编辑前的成员信息
		List<SysUser> bUserList = sysUserService.queryByHomeCode(bHomeCode);
		//获取编辑后的成员信息
		List<SysUser> lUserList = villageHome.getUserList();
		//户籍编号不可更改
		if(!bHomeCode.equals(lHomeCode))
		{
			result.setSuccess(false);
			result.setMessage("户籍编号不可更改！");
			return result;
		}
		if(!bHostId.equals(lHostId))
		{
			SysUser lHost = sysUserService.queryByIdnumber(lHostId);
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
		 	SysUser laterHost = sysUserService.queryByIdnumber(lHostId);
		 	SysUser bHost = sysUserService.queryByIdnumber(bHostId);

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
					villageRelation relation = new villageRelation();
					relation.setHostIdnumber(lHostId);
					relation.setIdnumber(luser.getIdnumber());
					relation.setHomeRelation(luser.getRelation());
					relation.setHomeCode(bHomeCode);
					villageRelationService.save(relation);
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
			SysUser host = sysUserService.queryByIdnumber(lHostId);
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
		//户主变动后，保存新户主和家庭成员的关系，否则只考虑更新了家庭关系的部分
		if(!bHostId.equals(lHostId))
		{
			List<SysUser> userList = sysUserService.queryByHomeCode(bHomeCode);
			for(int i=0;i<userList.size();i++)
			{
				SysUser user = userList.get(i);
				villageRelation relation = new villageRelation();
				relation.setHomeCode(bHomeCode);
				relation.setHostIdnumber(lHostId);
				relation.setIdnumber(user.getIdnumber());
				relation.setIdnumber(user.getId());
				villageRelationService.save(relation);
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
		SysUser host = sysUserService.queryByIdnumber(villageHome.getIdnumber());
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
			SysUser host = sysUserService.queryByIdnumber(villageHome.getIdnumber());
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
    public ModelAndView exportXls(HttpServletRequest request,villageHome villageHome) {
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
//        return super.importExcelForVillageHome(request, response, villageHome.class);
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				MultipartFile file = entity.getValue();// 获取上传文件对象
				ImportParams params = new ImportParams();
				params.setTitleRows(2);
				params.setHeadRows(1);
				params.setNeedSave(true);
				try {
//                InputStream inputStream = file.getInputStream();
//                Workbook wb = WorkbookFactory.create(inputStream);
//                //获取某个sheet
//                Sheet sheet = wb.getSheetAt(0);
//                //获取某一行
//                Row row = sheet.getRow(0);
//                //获取行的某一列
//                Cell cell0 = row.getCell(0);
//                //获取单元格中的字符串
//                RichTextString fileData = cell0.getRichStringCellValue();
					//获取villageHome类
					List<villageHome> list = ExcelImportUtil.importExcel(file.getInputStream(), villageHome.class, params);
					//获取user类
					List<SysUser> listForUser = ExcelImportUtil.importExcel(file.getInputStream(), SysUser.class , params);
					//获取Relation类
					List<villageRelation> listForRelation = ExcelImportUtil.importExcel(file.getInputStream(), villageRelation.class, params);
					//update-begin-author:taoyan date:20190528 for:批量插入数据
					long start = System.currentTimeMillis();
					//是否是户主，如果是户主，添加户籍和户主，否则添加家庭成员和家庭关系
					List<Integer> ifHost = listForUser.stream().map(e -> e.getHomeRole()).collect(Collectors.toList());
					List<Integer> oUse = listForUser.stream().map(e -> e.getHomeRole()).collect(Collectors.toList());
					List<Integer> tUse = listForUser.stream().map(e -> e.getHomeRole()).collect(Collectors.toList());
					List<Integer> thUse = listForUser.stream().map(e -> e.getHomeRole()).collect(Collectors.toList());
					List<Integer> fUse = listForUser.stream().map(e -> e.getHomeRole()).collect(Collectors.toList());
					for(int i=0;i<list.size();i++)
					{
						if(oUse.get(i) == 2)
						{
							list.remove(i);
							oUse.remove(i);
							i--;
						}
					}
					for(int i=0;i<list.size();i++)
					{
						villageHome home = list.get(i);
						home.setZhenId(sysDepartService.getZhenIdByName(home.getZhenId()));
						home.setDepartId(sysDepartService.getCunIdByNames(home.getZhenId(),home.getDepartId()));
						list.set(i,home);
					}
					villageHomeService.saveBatch(list);
					for(int i=0;i<listForUser.size();i++)
					{
						SysUser user = listForUser.get(i);
						if(user.getRole().equals("村民"))
						{
							user.setRole("1463112478345588738");
						}
						else if(user.getRole().equals("村长"))
						{
							user.setRole("1463074308371800066");
						}
						else{
							return Result.error("文件导入信息有误，用户角色不存在！");
						}
						user.setZhenId(sysDepartService.getZhenIdByName(user.getZhenId()));
						user.setDepartId(sysDepartService.getCunIdByNames(user.getZhenId(),user.getDepartId()));
						listForUser.set(i,user);
					}
					List<SysUser> listForHost = new ArrayList<>(listForUser);
					for(int i=0;i<listForHost.size();i++)
					{
						if(tUse.get(i) == 2)
						{
							listForHost.remove(i);
							tUse.remove(i);
							i--;
						}
					}
					for(int i=0;i<listForHost.size();i++)
					{
						SysUser host = listForHost.get(i);
//						host.setDelFlag(0);
//						host.setPeopleType("2");
//						host.setUsername(host.getPhone());
//						host.setPassword("123456");
//						listForHost.set(i,host);
						host.setPeopleType("2");
						String phone = host.getPhone();
						if(phone == null)
						{

						}else {
							//设置初始账号：手机号
							host.setUsername(phone);
							//设置初始密码
							host.setPassword("123456");
							String salt = oConvertUtils.randomGen(8);
							host.setSalt(salt);
							String passwordEncode = PasswordUtil.encrypt(host.getUsername(), host.getPassword(), salt);
							host.setPassword(passwordEncode);
						}
						host.setCreateTime(new Date());//设置创建时间
						host.setStatus(1);
						host.setDelFlag(CommonConstant.DEL_FLAG_0);
						if(host.getDepartId() != null && !host.getDepartId().equals("")) {
							SysDepart depart = sysDepartService.queryDeptByDepartId(host.getDepartId());
							System.out.println(host.getDepartId());
							String userOrgCode = depart.getOrgCode();
							host.setOrgCode(userOrgCode);
						}
						sysUserService.saveUser(host, host.getRole(), host.getDepartId());
						listForHost.set(i,host);
					}
//					sysUserService.saveBatch(listForHost);
					List<SysUser> listForMember = new ArrayList<>(listForUser);
					for(int i=0;i<listForMember.size();i++)
					{
						if(thUse.get(i) == 1)
						{
							listForMember.remove(i);
							thUse.remove(i);
							i--;
						}
					}
					for(int i=0;i<listForMember.size();i++)
					{
						SysUser member = listForMember.get(i);
//						member.setDelFlag(0);
//						member.setPeopleType("2");
//						member.setUsername(member.getPhone());
//						member.setPassword("123456");
//						listForMember.set(i,member);
						member.setPeopleType("2");
						String phone = member.getPhone();
						if(phone == null)
						{
							return Result.error("文件导入失败,户主电话号不可为空！");
						}else {
							//设置初始账号：手机号
							member.setUsername(phone);
							//设置初始密码
							member.setPassword("123456");
							String salt = oConvertUtils.randomGen(8);
							member.setSalt(salt);
							String passwordEncode = PasswordUtil.encrypt(member.getUsername(), member.getPassword(), salt);
							member.setPassword(passwordEncode);
						}
						member.setCreateTime(new Date());//设置创建时间
						member.setStatus(1);
						member.setDelFlag(CommonConstant.DEL_FLAG_0);
						if(member.getDepartId() != null && !member.getDepartId().equals("")) {
							SysDepart depart = sysDepartService.queryDeptByDepartId(member.getDepartId());
							String userOrgCode = depart.getOrgCode();
							depart.setOrgCode(userOrgCode);
						}
						sysUserService.saveUser(member, member.getRole(), member.getDepartId());
						listForMember.set(i,member);
					}
//					sysUserService.saveBatch(listForMember);
					for(int i=0;i<listForRelation.size();i++)
					{
						if(fUse.get(i) == 1)
						{
							listForRelation.remove(i);
							fUse.remove(i);
							i--;
						}
					}
					for(int i=0;i<listForRelation.size();i++)
					{
						villageRelation relation = listForRelation.get(i);
						relation.setHostIdnumber(villageHomeService.getHostByHomeCode(relation.getHomeCode()));
					}
					villageRelationService.saveBatch(listForRelation);
					//400条 saveBatch消耗时间1592毫秒  循环插入消耗时间1947毫秒
					//1200条  saveBatch消耗时间3687毫秒 循环插入消耗时间5212毫秒
					log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
					//update-end-author:taoyan date:20190528 for:批量插入数据
					return Result.ok("文件导入成功！数据行数：" + list.size());
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

}
