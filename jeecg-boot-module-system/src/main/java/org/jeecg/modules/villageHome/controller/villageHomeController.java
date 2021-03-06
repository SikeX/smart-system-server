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
import org.jeecg.common.util.ImportExcelUtil;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.common.util.ParamsUtil;
import org.jeecg.modules.smartFuneralReport.entity.SmartFuneralReport;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.villageHome.entity.villageHome;
import org.jeecg.modules.villageHome.mapper.villageHomeMapper;
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
 * @Description: ???????????????
 * @Author: jeecg-boot
 * @Date:   2021-12-24
 * @Version: V1.0
 */
@Api(tags="???????????????")
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

	 @Autowired
	 private villageHomeMapper homeMapper;

	 /**
	 * ??????????????????
	 *
	 * @param villageHome
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "???????????????-??????????????????")
	@ApiOperation(value="???????????????-??????????????????", notes="???????????????-??????????????????")
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
	 *   ??????
	 *
	 * @param villageHome
	 * @return
	 */
	@AutoLog(value = "???????????????-??????")
	@ApiOperation(value="???????????????-??????", notes="???????????????-??????")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody villageHome villageHome) {
		Result<?> result = new Result<>();
		//??????????????????????????????????????????
		SysUser host = sysUserService.queryByIdnumber(villageHome.getIdnumber());
		if(host.getHomeCode()!=null && !host.getHomeCode().equals(""))
		{
			result.setSuccess(false);
			result.setMessage("????????????????????????????????????");
			return result;
		}
		if(host.getPhone()==null || host.getPhone().equals(""))
		{
			result.setSuccess(false);
			result.setMessage("??????????????????????????????");
			return result;
		}
		//?????????????????????????????????????????????
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
						result.setMessage("??????????????????????????????????????????");
						return result;
					}
				}

			}
		}
		//????????????????????????+????????????
		String homeCode = villageHome.getHomeCode();
		host.setHomeCode(homeCode);
		host.setHomeRole(1);
		sysUserService.updateById(host);
		//??????????????????????????????+????????????
		if(!userList.isEmpty())
		{
			for(SysUser user:userList)
			{
					if (user.getId() != null && !user.getId().equals("")) {
						user.setHomeCode(homeCode);
						user.setHomeRole(2);
						sysUserService.updateById(user);
						if(user.getRelation()!=null)
						{
						villageRelation relation = new villageRelation();
						relation.setHomeRelation(user.getRelation());
						relation.setIdnumber(user.getIdnumber());
						relation.setHomeCode(homeCode);
						relation.setHostIdnumber(host.getIdnumber());
						villageRelationService.save(relation);}
					}
			}
		}
        //??????????????????
		villageHome.setUserList(null);
		villageHome.setRealname(null);
		villageHome.setPhone(null);
		villageHome.setRole(null);
		villageHomeService.save(villageHome);
		return Result.OK("???????????????");
	}
	
	/**
	 *  ??????
	 *
	 * @param villageHome
	 * @return
	 */
	@AutoLog(value = "???????????????-??????")
	@ApiOperation(value="???????????????-??????", notes="???????????????-??????")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody villageHome villageHome) {
		Result<?> result = new Result<>();
		//??????????????????????????????
		villageHome bVillageHome = villageHomeService.getById(villageHome.getId());
		//?????????????????????????????????
		String bHomeCode = bVillageHome.getHomeCode();
		//?????????????????????????????????
		String lHomeCode = villageHome.getHomeCode();
		//????????????????????????????????????
		String bHostId = bVillageHome.getIdnumber();
		//????????????????????????????????????
		String lHostId = villageHome.getIdnumber();
		//??????????????????????????????
		List<SysUser> bUserList = sysUserService.queryByHomeCode(bHomeCode);
		//??????????????????????????????
		List<SysUser> lUserList = villageHome.getUserList();
		//????????????????????????
		if(!bHomeCode.equals(lHomeCode))
		{
			result.setSuccess(false);
			result.setMessage("???????????????????????????");
			return result;
		}
		if(!bHostId.equals(lHostId))
		{
//			SysUser lHost = sysUserService.queryByIdnumber(lHostId);
//			if(lHost.getHomeCode() != null && !lHost.getHomeCode().equals(""))
//			{
//				result.setSuccess(false);
//				result.setMessage("????????????????????????????????????");
//				return result;
//			}
//			if(lHost.getPhone()==null || lHost.getPhone().equals(""))
//			{
//				result.setSuccess(false);
//				result.setMessage("??????????????????????????????");
//				return result;
//			}
			result.setSuccess(false);
			result.setMessage("?????????????????????");
			return result;
		}
		//?????????????????????????????????????????????????????????????????????????????????
		if(!lUserList.isEmpty())
		{
			//???????????????
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
							result.setMessage("???????????????????????????????????????");
							return result;
						}
					}
				}
			}
			//???????????????
			for(SysUser buser:bUserList)
			{
				String bUserId = buser.getId();
				for(SysUser luser: lUserList )
				{
					if(luser.getId()!=null || !luser.getId().equals("")){
						String lUserId = luser.getId();
						if(lUserId.equals(bUserId))
						{   if(buser.getRelation() == null && luser.getRelation() == null)
						     {
								 break;
							 }
							 if(buser.getRelation() == null)
							 {
								 sysUserService.updateById(luser);
								 villageRelation relation = new villageRelation();
								 relation.setHostIdnumber(lHostId);
								 relation.setIdnumber(luser.getIdnumber());
								 relation.setHomeRelation(luser.getRelation());
								 relation.setHomeCode(lHomeCode);
								 villageRelationService.save(relation);
								 break;
							 }
							if(luser.getRelation() == null)
							{
								sysUserService.updateById(luser);
								break;
							}
							if(!buser.getRelation().equals(luser.getRelation()))
							{
								sysUserService.updateById(luser);
								villageRelation relation = new villageRelation();
								relation.setHostIdnumber(lHostId);
								relation.setIdnumber(luser.getIdnumber());
								relation.setHomeRelation(luser.getRelation());
								relation.setHomeCode(lHomeCode);
								villageRelationService.save(relation);
								break;
							}
						}

					}
				}
			}
		}
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//		 if(!bHostId.equals(lHostId))
//		 {
//		 	SysUser laterHost = sysUserService.queryByIdnumber(lHostId);
//		 	SysUser bHost = sysUserService.queryByIdnumber(bHostId);
//
//		 	bHost.setHomeCode("");
//		 	bHost.setHomeRole(0);
//		 	sysUserService.updateById(bHost);
//		 	laterHost.setHomeCode(bHomeCode);
//		 	laterHost.setHomeRole(1);
//		 	sysUserService.updateById(laterHost);
//		 }
        //??????????????????????????????(??????????????????????????????????????????????????????????????????????????????????????????)
		//???????????????
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
					if(luser.getRelation()!=null)
					{
					villageRelation relation = new villageRelation();
					relation.setHostIdnumber(lHostId);
					relation.setIdnumber(luser.getIdnumber());
					relation.setHomeRelation(luser.getRelation());
					relation.setHomeCode(bHomeCode);
					villageRelationService.save(relation);}
				}
			}
		}
		//???????????????
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
				if(buser.getRelation()!=null)
				{buser.setRelation(-1);}
				sysUserService.updateById(buser);
			}
		}
        //??????????????????????????????,????????????????????????????????????????????????????????????home_code
//		if(!lHomeCode.equals(bHomeCode))
//		{
//			SysUser host = sysUserService.queryByIdnumber(lHostId);
//			host.setHomeCode(lHomeCode);
//			sysUserService.updateById(host);
//			for(SysUser user:lUserList)
//			{
//				if(user.getId()!=null && !user.getId().equals(""))
//				{user.setHomeCode(lHomeCode);
//				user.setHomeCode(lHomeCode);
//				sysUserService.updateById(user);}
//			}
//		}
//		//?????????????????????????????????????????????????????????????????????????????????????????????????????????
//		if(!bHostId.equals(lHostId))
//		{
//			List<SysUser> userList = sysUserService.queryByHomeCode(bHomeCode);
//			for(int i=0;i<userList.size();i++)
//			{
//				SysUser user = userList.get(i);
//				villageRelation relation = new villageRelation();
//				relation.setHomeCode(bHomeCode);
//				relation.setHostIdnumber(lHostId);
//				relation.setIdnumber(user.getIdnumber());
//				relation.setIdnumber(user.getId());
//				villageRelationService.save(relation);
//			}


		villageHome.setUserList(null);
		villageHome.setRealname(null);
		villageHome.setPhone(null);
		villageHome.setRole(null);
		villageHomeService.updateById(villageHome);
		return Result.OK("????????????!");
	}
	
	/**
	 *   ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "???????????????-??????id??????")
	@ApiOperation(value="???????????????-??????id??????", notes="???????????????-??????id??????")
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
			if(user.getRelation()!=null)
			{user.setRelation(-1);}
			sysUserService.updateById(user);
		}
		villageHomeService.removeById(id);
		return Result.OK("????????????!");
	}
	
	/**
	 *  ????????????
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "???????????????-????????????")
	@ApiOperation(value="???????????????-????????????", notes="???????????????-????????????")
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
				if(user.getRelation()!=null){
				user.setRelation(-1);}
				sysUserService.updateById(user);
			}
		}
		this.villageHomeService.removeByIds(villageHomeIds);
		return Result.OK("??????????????????!");
	}
	
	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "???????????????-??????id??????")
	@ApiOperation(value="???????????????-??????id??????", notes="???????????????-??????id??????")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		villageHome villageHome = villageHomeService.getById(id);
		if(villageHome==null) {
			return Result.error("?????????????????????");
		}
		return Result.OK(villageHome);
	}

    /**
    * ??????excel
    *
    * @param request
    * @param villageHome
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request,villageHome villageHome) {
		return super.exportXls(request, villageHome, villageHome.class, "???????????????");
    }

    /**
      * ??????excel????????????
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		// ????????????
		List<String> errorMessage = new ArrayList<>();
		int errorLines = 0;
			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				MultipartFile file = entity.getValue();// ????????????????????????
				ImportParams params = new ImportParams();
				params.setTitleRows(2);
				params.setHeadRows(1);
				params.setNeedSave(true);
				try {
					//??????villageHome???
					List<villageHome> list = ExcelImportUtil.importExcel(file.getInputStream(), villageHome.class, params);
					//??????user???
					List<SysUser> listForUser = ExcelImportUtil.importExcel(file.getInputStream(), SysUser.class , params);
					//??????Relation???
					List<villageRelation> listForRelation = ExcelImportUtil.importExcel(file.getInputStream(), villageRelation.class, params);
					//update-begin-author:taoyan date:20190528 for:??????????????????
					long start = System.currentTimeMillis();
					//???????????????????????????????????????????????????????????????????????????????????????????????????
					List<Integer> ifHost = listForUser.stream().map(e -> e.getHomeRole()).collect(Collectors.toList());
					List<Integer> oUse = listForUser.stream().map(e -> e.getHomeRole()).collect(Collectors.toList());
					List<Integer> tUse = listForUser.stream().map(e -> e.getHomeRole()).collect(Collectors.toList());
					List<Integer> thUse = listForUser.stream().map(e -> e.getHomeRole()).collect(Collectors.toList());
					List<Integer> fUse = listForUser.stream().map(e -> e.getHomeRole()).collect(Collectors.toList());
					for(int i=0,len=list.size();i<len;i++)
					{
						if(oUse.get(i) == 2)
						{
							list.remove(i);
							oUse.remove(i);
							i--;
							len--;
						}
					}
					for(int i=0;i<list.size();i++)
					{
						villageHome home = list.get(i);
						if(homeMapper.getByHomeCode(home.getHomeCode())!=null)
						{
							return Result.error("????????????:?????????"+home.getHomeCode()+"??????????????????");
						}
						if(sysUserService.queryByIdnumber(home.getIdnumber())!=null)
						{
							return Result.error("????????????:???????????????"+home.getIdnumber()+"??????????????????");
						}
						if(home.getPhone()==""||home.getPhone()==null)
						{
							return Result.error("????????????:???????????????"+home.getIdnumber()+"??????????????????????????????????????????");
						}
						if(sysUserService.getUserByPhone(home.getPhone())!=null)
						{
							return Result.error("????????????:???????????????"+home.getIdnumber()+"???????????????????????????????????????");
						}
//						home.setZhenId(sysDepartService.getZhenIdByName(home.getZhenId()));
						home.setDepartId(sysDepartService.getCunIdByNames(home.getZhenId(),home.getDepartId()));
						list.set(i,home);
					}
					villageHomeService.saveBatch(list);
					for(int i=0;i<listForUser.size();i++)
					{
						SysUser user = listForUser.get(i);
						if(user.getRole().equals("??????"))
						{
							user.setRole("1463112478345588738");
						}
						else if(user.getRole().equals("??????"))
						{
							user.setRole("1463074308371800066");
						}
						else{
							errorLines++;
							errorMessage.add("??????????????? " + user.getIdnumber() + "??????????????????????????????????????????????????????????????????");
						}
						user.setDepartId(sysDepartService.getCunIdByNames(user.getZhenId(),user.getDepartId()));
						listForUser.set(i,user);
					}
					List<SysUser> listForHost = new ArrayList<>(listForUser);
					for(int i=0,len=listForHost.size();i<len;i++)
					{
						if(tUse.get(i) == 2)
						{
							listForHost.remove(i);
							tUse.remove(i);
							i--;
							len--;
						}
					}
					for(int i=0;i<listForHost.size();i++)
					{
						SysUser host = listForHost.get(i);
						host.setPeopleType("2");
						String phone = host.getPhone();
						if(phone == null)
						{

						}else {
							//??????????????????????????????
							host.setUsername(phone);
							//??????????????????
							host.setPassword("123456");
							String salt = oConvertUtils.randomGen(8);
							host.setSalt(salt);
							String passwordEncode = PasswordUtil.encrypt(host.getUsername(), host.getPassword(), salt);
							host.setPassword(passwordEncode);
						}
						host.setCreateTime(new Date());//??????????????????
						host.setStatus(1);
						host.setDelFlag(CommonConstant.DEL_FLAG_0);
						if(host.getDepartId() != null && !host.getDepartId().equals("")) {
							SysDepart depart = sysDepartService.queryDeptByDepartId(host.getDepartId());
							System.out.println(host.getDepartId());
							String userOrgCode = depart.getOrgCode();
							host.setOrgCode(userOrgCode);
						}
						sysUserService.saveUser(host, host.getRole(), host.getDepartId());
					}
					List<SysUser> listForMember = new ArrayList<>(listForUser);
					for(int i=0,len=listForMember.size();i<len;i++)
					{
						if(thUse.get(i) == 1)
						{
							listForMember.remove(i);
							thUse.remove(i);
							i--;
							len--;
						}
					}
					for(int i=0;i<listForMember.size();i++)
					{
						SysUser member = listForMember.get(i);
						if(sysUserService.queryByIdnumber(member.getIdnumber())!=null)
						{
							errorLines++;
							errorMessage.add("??????????????? " + member.getIdnumber() + "??????????????????????????????????????????????????????????????????????????????");
						}else if(sysUserService.getUserByPhone(member.getPhone())!=null)
						{
							errorLines++;
							errorMessage.add("??????????????? " + member.getIdnumber() + "???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
						}
						else{
						member.setPeopleType("2");
						String phone = member.getPhone();
						if(phone == null)
						{

						}else {
							//??????????????????????????????
							member.setUsername(phone);
							//??????????????????
							member.setPassword("123456");
							String salt = oConvertUtils.randomGen(8);
							member.setSalt(salt);
							String passwordEncode = PasswordUtil.encrypt(member.getUsername(), member.getPassword(), salt);
							member.setPassword(passwordEncode);
						}
						member.setCreateTime(new Date());//??????????????????
						member.setStatus(1);
						member.setDelFlag(CommonConstant.DEL_FLAG_0);
						if(member.getDepartId() != null && !member.getDepartId().equals("")) {
							SysDepart depart = sysDepartService.queryDeptByDepartId(member.getDepartId());
							String userOrgCode = depart.getOrgCode();
							depart.setOrgCode(userOrgCode);
						}
						sysUserService.saveUser(member, member.getRole(), member.getDepartId());}
					}

					for(int i=0,len=listForRelation.size();i<len;i++)
					{
						if(fUse.get(i) == 1)
						{
							listForRelation.remove(i);
							fUse.remove(i);
							i--;
							len--;
						}
					}
					for(int i=0;i<listForRelation.size();i++)
					{
						villageRelation relation = listForRelation.get(i);
						relation.setHostIdnumber(villageHomeService.getHostByHomeCode(relation.getHomeCode()));
					}
					villageRelationService.saveBatch(listForRelation);
					//400??? saveBatch????????????1592??????  ????????????????????????1947??????
					//1200???  saveBatch????????????3687?????? ????????????????????????5212??????
					log.info("????????????" + (System.currentTimeMillis() - start) + "??????");
					//update-end-author:taoyan date:20190528 for:??????????????????
				} catch (Exception e) {
					errorLines++;
					errorMessage.add("????????????,??????????????????????????????????????????????????????" + e.getMessage());
					log.error(e.getMessage(), e);
				} finally {
					try {
						file.getInputStream().close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return ImportExcelUtil.importReturnRes(errorLines,errorMessage);

    }

}
