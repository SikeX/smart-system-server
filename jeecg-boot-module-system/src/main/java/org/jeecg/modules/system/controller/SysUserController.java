package org.jeecg.modules.system.controller;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.*;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.common.service.CommonService;
import org.jeecg.modules.common.util.ParamsUtil;
import org.jeecg.modules.smartReportingInformation.entity.SysRole;
import org.jeecg.modules.smart_window_people.entity.SmartWindowPeople;
import org.jeecg.modules.smart_window_people.service.ISmartWindowPeopleService;
import org.jeecg.modules.smart_window_unit.entity.SmartWindowUnit;
import org.jeecg.modules.smart_window_unit.service.ISmartWindowUnitService;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.SysDepartMapper;
import org.jeecg.modules.system.model.DepartIdModel;
import org.jeecg.modules.system.model.SysUserSysDepartModel;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.vo.SysDepartUsersVO;
import org.jeecg.modules.system.vo.SysUserRoleVO;
import org.jeecg.modules.system.vo.SysUserVo;
import org.jeecg.modules.system.vo.VillageUser;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * ????????? ???????????????
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@Slf4j
@RestController
@RequestMapping("/sys/user")
public class SysUserController {
	@Autowired
	private ISysBaseAPI sysBaseAPI;
	
	@Autowired
	private ISysUserService sysUserService;

    @Autowired
    private ISysDepartService sysDepartService;

	@Autowired
	private ISysUserRoleService sysUserRoleService;

	@Autowired
	private ISysUserDepartService sysUserDepartService;

	@Autowired
	private ISysUserRoleService userRoleService;

    @Autowired
    private ISysDepartRoleUserService departRoleUserService;

    @Autowired
    private ISysDepartRoleService departRoleService;

	@Autowired
	private RedisUtil redisUtil;
    @Autowired
    private CommonService commonService;
    @Autowired
    private BaseCommonService baseCommon_Service;

    @Autowired
    private ISmartWindowPeopleService smartWindowPeopleService;

    @Autowired
    private ISmartWindowUnitService smartWindowUnitService;

    @Value("${jeecg.path.upload}")
    private String upLoadPath;

    @Resource
    private BaseCommonService baseCommonService;
    @Autowired
    private SysDepartMapper sysDepartMapper;

    /**
     * ????????????????????????
     * @param user
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @PermissionData(pageComponent = "system/UserList")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<IPage<SysUser>> queryPageList(SysUser user,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
//        HashMap<String, String[]> map = new HashMap<>(req.getParameterMap());
        // ???????????????????????????????????????????????????????????????
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // ??????????????????
        String userName = sysUser.getUsername();
        List<String> role = sysBaseAPI.getRolesByUsername(userName);
        QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(user, req.getParameterMap());
        //?????????????????????????????????????????????????????????
        if(role.contains("CCDIAdmin") || role.contains("systemAdmin")){
        }
        //???????????????-??????????????????????????????????????????
        else if(role.contains("admin")){
//            String ordCode = sysUser.getOrgCode();
//            String departId = commonService.getDepartIdByOrgCode(ordCode);
            //??????????????????ID
            String departId = sysUserService.getById(sysUser.getId()).getDepartIds();
            List<String> list = Arrays.asList(departId.split(","));
            if(list.size()==1){
                queryWrapper.like("depart_id",departId);
            }else if(list.size()>1){
                queryWrapper.like("depart_id",list.get(0));
                for(int i =1;i < list.size();i++){
                    queryWrapper.or().like("depart_id",list.get(i));
                }
            }
           /* // 1. ?????????????????? ???**??????
            String rule = "in";
            // 2. ????????????
            String field = "departId";
            //?????????????????????ID
            String departId = sysUser.getDepartId();
            // ???????????????ID
            //String childrenIdString = commonService.getChildrenIdStringByOrgCode(sysUser.getOrgCode());
            // ????????????????????????superQueryParams
            List<String> paramsList = ParamsUtil.getSuperQueryParams(req.getParameterMap());
            // ?????????????????????????????????????????????
            *//*paramsList.add("%5B%7B%22rule%22:%22" + rule + "%22,%22type%22:%22string%22,%22dictCode%22:%22%22,%22val%22:%22"
                    + childrenIdString
                    + "%22,%22field%22:%22" + field + "%22%7D%5D");*//*
            paramsList.add("%5B%7B%22rule%22:%22" + rule + "%22,%22type%22:%22string%22,%22dictCode%22:%22%22,%22val%22:%22"
                    + departId
                    + "%22,%22field%22:%22" + field + "%22%7D%5D");
            String[] params = new String[paramsList.size()];
            paramsList.toArray(params);
            map.put("superQueryParams", params);
            params = new String[]{"and"};
            map.put("superQueryMatchType", params);*/
        }

        Result<IPage<SysUser>> result = new Result<IPage<SysUser>>();
    	//TODO ????????????????????????????????????????????????
        queryWrapper.ne("username","_reserve_user_external");
		Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
		IPage<SysUser> pageList = sysUserService.page(page, queryWrapper);

		//?????????????????????????????????
        //step.1 ?????????????????? useids
        //step.2 ?????? useids?????????????????????????????????????????????
        List<String> userIds = pageList.getRecords().stream().map(SysUser::getId).collect(Collectors.toList());
        if(userIds.size()>0){
            Map<String,String>  useDepNames = sysUserService.getDepNamesByUserIds(userIds);
            pageList.getRecords().forEach(item->{
                item.setOrgCodeTxt(useDepNames.get(item.getId()));
            });
        }
		result.setSuccess(true);
		result.setResult(pageList);
		log.info(pageList.toString());
        // ???????????????edit???????????????departId??????null???????????????????????????
        List<String> departIds = pageList.getRecords().stream().map(SysUser::getDepartId).collect(Collectors.toList());
        if (departIds != null && departIds.size() > 0) {
            Map<String, String> useDepNames = commonService.getDepNamesByIds(departIds);
            pageList.getRecords().forEach(item -> {
                item.setDepartId(useDepNames.get(item.getDepartId()));
            });
        }
		return result;
	}

    /**
     * ????????????????????????
     * @param user
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @PermissionData(pageComponent = "system/peopleList")
    @RequestMapping(value = "/peopleList", method = RequestMethod.GET)
    public Result<IPage<SysUser>> queryPeopleList(SysUser user,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
//        // 1. ?????????????????? ???**??????
//        String rule = "in";
//        // 2. ????????????
//        String field = "departId";
//        // ???????????????????????????????????????????????????????????????
//        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//        // ???????????????ID
//        String childrenIdString = commonService.getChildrenIdStringByOrgCode(sysUser.getOrgCode());
        HashMap<String, String[]> map = new HashMap<>(req.getParameterMap());
        // ????????????????????????superQueryParams
//        List<String> paramsList = ParamsUtil.getSuperQueryParams(req.getParameterMap());
//        // ?????????????????????????????????????????????
//        paramsList.add("%5B%7B%22rule%22:%22" + rule + "%22,%22type%22:%22string%22,%22dictCode%22:%22%22,%22val%22:%22"
//                + childrenIdString
//                + "%22,%22field%22:%22" + field + "%22%7D%5D");
//
//        String[] params = new String[paramsList.size()];
//        paramsList.toArray(params);
//        map.put("superQueryParams", params);
//        params = new String[]{"and"};
//        map.put("superQueryMatchType", params);

        Result<IPage<SysUser>> result = new Result<IPage<SysUser>>();
        QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(user,map);
        //TODO ????????????????????????????????????????????????
        queryWrapper.ne("username","_reserve_user_external");
        Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
        IPage<SysUser> pageList = sysUserService.page(page, queryWrapper);

        //?????????????????????????????????
        //step.1 ?????????????????? useids
        //step.2 ?????? useids?????????????????????????????????????????????
        List<String> userIds = pageList.getRecords().stream().map(SysUser::getId).collect(Collectors.toList());
        if(userIds!=null && userIds.size()>0){
            Map<String,String>  useDepNames = sysUserService.getDepNamesByUserIds(userIds);
            pageList.getRecords().forEach(item->{
                item.setOrgCodeTxt(useDepNames.get(item.getId()));
            });
        }
        result.setSuccess(true);
        result.setResult(pageList);
        log.info(pageList.toString());
        // ???????????????edit???????????????departId??????null???????????????????????????
//        List<String> departIds = pageList.getRecords().stream().map(SysUser::getDepartId).collect(Collectors.toList());
//        if (departIds != null && departIds.size() > 0) {
//            Map<String, String> useDepNames = commonService.getDepNamesByIds(departIds);
//            pageList.getRecords().forEach(item -> {
//                item.setDepartId(useDepNames.get(item.getDepartId()));
//            });
//        }
        return result;
    }


    //@RequiresRoles({"admin"})
    //@RequiresPermissions("user:add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Result<SysUser> add(@RequestBody JSONObject jsonObject) {
		Result<SysUser> result = new Result<SysUser>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String orgCode = sysUser.getOrgCode();
        System.out.println("sysUser:"+sysUser);
        // ??????????????????
        String userName = sysUser.getUsername();
        List<String> role = sysBaseAPI.getRolesByUsername(userName);
        if ("".equals(orgCode)) {
            return result.error500("??????????????????????????????");
        }
        String id = commonService.getDepartIdByOrgCode(orgCode);
        if (id == null) {
            return result.error500("?????????????????????");
        }
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        String selectedRoles = "";
        String selectedDeparts="";
        if(role.contains("CCDIAdmin") || role.contains("systemAdmin")){
            selectedRoles = jsonObject.getString("selectedroles");
            selectedDeparts = jsonObject.getString("selecteddeparts");

            String departId = selectedDeparts.split(",",-1)[0];
            String orgCodes = sysDepartService.getById(departId).getOrgCode();
            jsonObject.put("orgCode",orgCodes);
        }
		else if(role.contains("admin")){
            selectedRoles = "1465163864583323650";
            //???????????????-??????????????????????????????????????????
            selectedDeparts = sysUser.getDepartIds();

            jsonObject.put("orgCode",sysDepartService.getById(selectedDeparts).getOrgCode());
        }

		try {
			SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
			//??????user?????????departId
			user.setDepartId(selectedDeparts);
			//??????????????????,????????????
            user.setPeopleType("1");
			//??????sys_code
			user.setCreateTime(new Date());//??????????????????
            String phone = user.getPhone();
            String username = user.getUsername();
            //??????????????????????????????
            if(username == null || username.isEmpty()){
                user.setUsername(phone);
            }
            //??????????????????
            String password = user.getPassword();
            if(password == null || password.isEmpty()){
                user.setPassword("123456");
            }
			String salt = oConvertUtils.randomGen(8);
			user.setSalt(salt);
			String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
			user.setPassword(passwordEncode);
			user.setStatus(1);
			user.setDelFlag(CommonConstant.DEL_FLAG_0);
            System.out.println("addUser:"+user);
			// ?????????????????????service ?????????????????????user???????????????
            sysUserService.saveUser(user, selectedRoles, selectedDeparts);
			result.success("???????????????");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("????????????");
		}
		return result;
	}


    //@RequiresRoles({"admin"})
    //@RequiresPermissions("user:edit")
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	public Result<SysUser> edit(@RequestBody JSONObject jsonObject) {
		Result<SysUser> result = new Result<SysUser>();

		try {
			SysUser sysUser = sysUserService.getById(jsonObject.getString("id"));
			baseCommonService.addLog("???????????????id??? " +jsonObject.getString("id") ,CommonConstant.LOG_TYPE_2, 2);
			if(sysUser==null) {
				result.error500("?????????????????????");
			}else {
				SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
                user.setDepartId(null);
                //user.setCreateTime(null);
				user.setUpdateTime(new Date());
				//String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), sysUser.getSalt());
				user.setPassword(sysUser.getPassword());
				String roles = jsonObject.getString("selectedroles");
                String departs = jsonObject.getString("selecteddeparts");
                //???????????????????????????????????????????????????????????????user??????departId
                if(departs != null && !departs.isEmpty()){
                    user.setDepartId(departs);
                }
                // ?????????????????????service ????????????
				sysUserService.editUser(user, roles, departs);
				result.success("????????????!");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("????????????");
		}
        SentInfCauseU(jsonObject.getString("id"));
        SentInfCauseP(jsonObject.getString("id"));
//        QueryWrapper<SmartWindowUnit> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("principal",jsonObject.getString("id"));
//        List<SmartWindowUnit> unit = smartWindowUnitService.list(queryWrapper);
//
//        for(SmartWindowUnit U : unit){
//            if(U.getId() == null){
//                break;
//            }
//            else {
//                List<SysRole> role=smartWindowUnitService.getUser();
//                for(SysRole r:role) {
//                    try {
//                        MessageDTO messageDTO = new MessageDTO();
//                        messageDTO.setTitle("???????????????????????????????????????");
//                        messageDTO.setContent("????????????????????????:"+U.getPName() + "???,??????????????????" + U.getPrincipalName() +"???????????????????????????????????????????????????????????????");
//                        messageDTO.setFromUser("admin");
//                        messageDTO.setToUser(r.getUsername());
//                        messageDTO.setCategory("1");
//
//                        sysBaseAPI.sendSysAnnouncement(messageDTO);
//
//                    } catch (NullPointerException e) {
//                    }
//                }
//            }
//        }

//        QueryWrapper<SmartWindowPeople> queryWrapper2 = new QueryWrapper<>();
//        queryWrapper2.eq("person_id",jsonObject.getString("id"));
//        List<SmartWindowPeople> person = smartWindowPeopleService.list(queryWrapper2);
//        for(SmartWindowPeople P : person){
//            if(P.getId() == null){
//                break;
//            }
//            else {
//                List<SysRole> role2 = smartWindowPeopleService.getUser();
//                for(SysRole r2 : role2) {
//                    String departmentId = P.getDepartmentId();
//                    String windowName = smartWindowPeopleService.getDepartmentNameByDepartmentId(departmentId);
//                    try {
//                        MessageDTO messageDTO = new MessageDTO();
//                        messageDTO.setTitle("??????????????????????????????");
//                        messageDTO.setContent("????????????????????????:"+windowName + "??????" + P.getPersonName() +"?????????????????????????????????");
//                        messageDTO.setFromUser("admin");
//                        messageDTO.setToUser(r2.getUsername());
//                        messageDTO.setCategory("1");
//
//                        sysBaseAPI.sendSysAnnouncement(messageDTO);
//
//                    } catch (NullPointerException e) {
//                    }
//                }
//            }
//        }
		return result;
	}

    //@RequiresRoles({"admin"})
    //@RequiresPermissions("user:edit")
    @RequestMapping(value = "/editPeople", method = RequestMethod.PUT)
    public Result<SysUser> editPeople(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<SysUser>();

        try {
            SysUser sysUser = sysUserService.getById(jsonObject.getString("id"));
            baseCommonService.addLog("???????????????id??? " +jsonObject.getString("id") ,CommonConstant.LOG_TYPE_2, 2);
            if(sysUser==null) {
                result.error500("?????????????????????");
            }else {
                SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
                user.setDepartId(null);
                //user.setCreateTime(null);
                user.setUpdateTime(new Date());
                //String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), sysUser.getSalt());
                user.setPassword(sysUser.getPassword());
                String roles = jsonObject.getString("selectedroles");
                String departs = jsonObject.getString("selecteddeparts");
                //???????????????????????????????????????????????????????????????user??????departId
                if(departs != null && !departs.isEmpty()){
                    user.setDepartId(departs);
                    SysDepart depart =  sysDepartService.queryDeptByDepartId(user.getDepartId());
                    String userOrgCode = depart.getOrgCode();
                    user.setOrgCode(userOrgCode);
                }else
                {
                    user.setOrgCode("");
                }
                // ?????????????????????service ????????????
                sysUserService.editUser(user, roles, departs);
                result.success("????????????!");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("????????????");
        }
        SentInfCauseU(jsonObject.getString("id"));
        SentInfCauseP(jsonObject.getString("id"));
        return result;
    }
	/**
	 * ????????????
	 */
	//@RequiresRoles({"admin"})
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		baseCommonService.addLog("???????????????id??? " +id ,CommonConstant.LOG_TYPE_2, 3);
		this.sysUserService.deleteUser(id);
        SentInfCauseU(id);
        SentInfCauseP(id);
		return Result.ok("??????????????????");
	}

    @PermissionData(pageComponent = "system/villagePeopleList")
    @RequestMapping(value = "/villageList", method = RequestMethod.GET)
    public Result<IPage<SysUser>> queryVillageList(SysUser user,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {

        Result<IPage<SysUser>> result = new Result<IPage<SysUser>>();
        // ???????????????????????????????????????????????????????????????
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // ??????????????????
        String userName = sysUser.getUsername();
        List<String> role = sysBaseAPI.getRolesByUsername(userName);
        HashMap<String, String[]> map = new HashMap<>(req.getParameterMap());
        if(role.contains("CCDIAdmin")){

        }else {
            // 1. ?????????????????? ???**??????
            String rule = "in";
            // 2. ????????????
            String field = "departId";
            String childrenIdString = null;
            if (sysUser.getDepartId() != null && !"".equals(sysUser.getDepartId())) {
                String userOrgCode = sysDepartService.getById(sysUser.getDepartId()).getOrgCode();
                // ???????????????ID
                childrenIdString = commonService.getChildrenIdStringByOrgCode(userOrgCode);
            } else {
                return result.error500("?????????????????????????????????????????????");
            }

            // ????????????????????????superQueryParams
            List<String> paramsList = ParamsUtil.getSuperQueryParams(req.getParameterMap());

            // ?????????????????????????????????????????????
            paramsList.add("%5B%7B%22rule%22:%22" + rule + "%22,%22type%22:%22string%22,%22dictCode%22:%22%22,%22val%22:%22"
                    + childrenIdString
                    + "%22,%22field%22:%22" + field + "%22%7D%5D");
            String[] params = new String[paramsList.size()];
            paramsList.toArray(params);
            map.put("superQueryParams", params);
            params = new String[]{"and"};
            map.put("superQueryMatchType", params);
        }
            QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(user, map);
            //TODO ????????????????????????????????????????????????
//            queryWrapper.ne("username", "_reserve_user_external");
            Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
            IPage<SysUser> pageList = sysUserService.page(page, queryWrapper);

        //?????????????????????????????????
        //step.1 ?????????????????? useids
        //step.2 ?????? useids?????????????????????????????????????????????
        List<String> userIds = pageList.getRecords().stream().map(SysUser::getId).collect(Collectors.toList());
        if(userIds!=null && userIds.size()>0){
            Map<String,String>  useDepNames = sysUserService.getDepNamesByUserIds(userIds);
            pageList.getRecords().forEach(item->{
                item.setOrgCodeTxt(useDepNames.get(item.getId()));
            });
        }
        result.setSuccess(true);
        result.setResult(pageList);
        log.info(pageList.toString());
        // ???????????????edit???????????????departId??????null???????????????????????????
        List<String> departIds = pageList.getRecords().stream().map(SysUser::getDepartId).collect(Collectors.toList());
        if (departIds != null && departIds.size() > 0) {
            Map<String, String> useDepNames = commonService.getDepNamesByIds(departIds);
            pageList.getRecords().forEach(item -> {
                item.setDepartId(useDepNames.get(item.getDepartId()));
            });
        }

        return result;
    }

    @RequestMapping(value = "/addVillage", method = RequestMethod.POST)
    public Result<SysUser> addVillage(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<SysUser>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String orgCode = sysUser.getOrgCode();
        if ("".equals(orgCode)) {
            return result.error500("??????????????????????????????");
        }
        String selectedRoles = jsonObject.getString("selectedroles");
        String selectedDeparts = jsonObject.getString("selecteddeparts");
        try {
            SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
            user.setPeopleType("2");
            String phone = user.getPhone();
            if(phone == null)
            {
            }else {
                //??????????????????????????????
                user.setUsername(phone);
                //??????????????????
                user.setPassword("123456");
                String salt = oConvertUtils.randomGen(8);
                user.setSalt(salt);
                String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
                user.setPassword(passwordEncode);
            }
            user.setCreateTime(new Date());//??????????????????
            user.setStatus(1);
            user.setDelFlag(CommonConstant.DEL_FLAG_0);
            user.setDepartId(selectedDeparts);
            SysDepart depart =  sysDepartService.queryDeptByDepartId(user.getDepartId());
            String userOrgCode = depart.getOrgCode();
            user.setOrgCode(userOrgCode);
            user.setRole(selectedRoles);
            sysUserService.saveUser(user, selectedRoles, selectedDeparts);
            result.success("???????????????");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("????????????");
        }
        return result;
    }

    @RequestMapping(value = "/addPeople", method = RequestMethod.POST)
    public Result<SysUser> addPeople(@RequestBody JSONObject jsonObject) {
	    Result<SysUser> result = new Result<SysUser>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String orgCode = sysUser.getOrgCode();
        if ("".equals(orgCode)) {
            return result.error500("??????????????????????????????");
        }

        String selectedRoles = jsonObject.getString("selectedroles");
        String selectedDeparts = jsonObject.getString("selecteddeparts");
        try {
            SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
            user.setPeopleType("1");
            user.setCreateTime(new Date());//??????????????????
            String phone = user.getPhone();
            String username = user.getUsername();
            //??????????????????????????????
            if(username == null){
                user.setUsername(phone);
            }
            //??????????????????
            String password = user.getPassword();
            if(password == null){
                user.setPassword("123456");
            }
            String salt = oConvertUtils.randomGen(8);
            user.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
            user.setPassword(passwordEncode);
            user.setStatus(1);
            user.setDelFlag(CommonConstant.DEL_FLAG_0);
            user.setDepartId(selectedDeparts);
            SysDepart depart =  sysDepartService.queryDeptByDepartId(user.getDepartId());
            String userOrgCode = depart.getOrgCode();
            user.setOrgCode(userOrgCode);
            sysUserService.saveUser(user, selectedRoles, selectedDeparts);
            result.success("???????????????");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("????????????");
        }
        return result;
    }

    @RequestMapping(value = "/editVillage", method = RequestMethod.PUT)
    public Result<SysUser> editVillage(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<SysUser>();

        try {
            SysUser sysUser = sysUserService.getById(jsonObject.getString("id"));
            baseCommonService.addLog("???????????????id??? " +jsonObject.getString("id") ,CommonConstant.LOG_TYPE_2, 2);
            if(sysUser==null) {
                result.error500("?????????????????????");
            }else {
                SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
                user.setDepartId(jsonObject.getString("selecteddeparts"));
                //user.setCreateTime(null);
                user.setUpdateTime(new Date());
                //String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), sysUser.getSalt());
                String phone = user.getPhone();
               String oldPhone = sysUser.getPhone();
                if(oldPhone!=null && !oldPhone.equals("") && phone.equals(sysUser.getPhone())){
                    user.setPassword(sysUser.getPassword());
                }
                else if(phone==null || phone.equals(""))
                {
                    user.setPassword(null);
                    user.setUsername(null);
                }
                else{
                    //????????????????????????
                    user.setUsername(phone);
                    //????????????
                    user.setPassword("123456");
                    String salt = oConvertUtils.randomGen(8);
                    user.setSalt(salt);
                    String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
                    user.setPassword(passwordEncode);
                }
                SysDepart depart =  sysDepartService.queryDeptByDepartId(user.getDepartId());
                String userOrgCode = depart.getOrgCode();
                user.setOrgCode(userOrgCode);
                user.setRole(jsonObject.getString("selectedroles"));
                String roles = jsonObject.getString("selectedroles");
                String departs = jsonObject.getString("selecteddeparts");
                // ?????????????????????service ????????????
                sysUserService.editUser(user, roles, departs);
                result.success("????????????!");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("????????????");
        }
        return result;
    }


    /**
	 * ??????????????????
	 */
	//@RequiresRoles({"admin"})
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		baseCommonService.addLog("????????????????????? ids??? " +ids ,CommonConstant.LOG_TYPE_2, 3);
		this.sysUserService.deleteBatchUsers(ids);

        List<String> list = Arrays.asList(ids.split(","));
        for(String L : list){
            SentInfCauseU(L);
            SentInfCauseP(L);
        }
		return Result.ok("????????????????????????");


	}

	/**
	  * ??????&????????????
	 * @param jsonObject
	 * @return
	 */
	//@RequiresRoles({"admin"})
	@RequestMapping(value = "/frozenBatch", method = RequestMethod.PUT)
	public Result<SysUser> frozenBatch(@RequestBody JSONObject jsonObject) {
		Result<SysUser> result = new Result<SysUser>();
		try {
			String ids = jsonObject.getString("ids");
			String status = jsonObject.getString("status");
			String[] arr = ids.split(",");
			for (String id : arr) {
				if(oConvertUtils.isNotEmpty(id)) {
					this.sysUserService.update(new SysUser().setStatus(Integer.parseInt(status)),
							new UpdateWrapper<SysUser>().lambda().eq(SysUser::getId,id));
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("????????????"+e.getMessage());
		}
        SentInfCauseU(jsonObject.getString("id"));
        SentInfCauseP(jsonObject.getString("id"));
		result.success("????????????!");
		return result;

    }

    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public Result<SysUser> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<SysUser> result = new Result<SysUser>();
        SysUser sysUser = sysUserService.getById(id);
        if (sysUser == null) {
            result.error500("?????????????????????");
        } else {
            result.setResult(sysUser);
            result.setSuccess(true);
        }
        return result;
    }

    @RequestMapping(value = "/queryUserRole", method = RequestMethod.GET)
    public Result<List<String>> queryUserRole(@RequestParam(name = "userid", required = true) String userid) {
        Result<List<String>> result = new Result<>();
        List<String> list = new ArrayList<String>();
        List<SysUserRole> userRole = sysUserRoleService.list(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, userid));
        if (userRole == null || userRole.size() <= 0) {
            result.error500("?????????????????????????????????");
        } else {
            for (SysUserRole sysUserRole : userRole) {
                list.add(sysUserRole.getRoleId());
            }
            result.setSuccess(true);
            result.setResult(list);
        }
        return result;
    }


    /**
	  *  ??????????????????????????????<br>
	  *  ?????????????????? ???????????????????????????????????????
     *
     * @param sysUser
     * @return
     */
    @RequestMapping(value = "/checkOnlyUser", method = RequestMethod.GET)
    public Result<Boolean> checkOnlyUser(SysUser sysUser) {
        Result<Boolean> result = new Result<>();
        //??????????????????false?????????????????????
        result.setResult(true);
        try {
            //??????????????????????????????????????????
            sysUser.setPassword(null);
            SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>(sysUser));
            if (user != null) {
                result.setSuccess(false);
                result.setMessage("?????????????????????");
                return result;
            }

        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    /**
     * ????????????
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/changePassword", method = RequestMethod.PUT)
    public Result<?> changePassword(@RequestBody SysUser sysUser) {
        SysUser u = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, sysUser.getUsername()));
        if (u == null) {
            return Result.error("??????????????????");
        }
        sysUser.setId(u.getId());
        return sysUserService.changePassword(sysUser);
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/userDepartList", method = RequestMethod.GET)
    public Result<List<DepartIdModel>> getUserDepartsList(@RequestParam(name = "userId", required = true) String userId) {
        Result<List<DepartIdModel>> result = new Result<>();
        try {
            List<DepartIdModel> depIdModelList = this.sysUserDepartService.queryDepartIdsOfUser(userId);
            if (depIdModelList != null && depIdModelList.size() > 0) {
                result.setSuccess(true);
                result.setMessage("????????????");
                result.setResult(depIdModelList);
            } else {
                result.setSuccess(false);
                result.setMessage("????????????");
            }
            return result;
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("??????????????????????????????: " + e.getMessage());
            return result;
        }

    }

    /**
     * ???????????????????????????????????????????????????,???????????????,?????????id??????????????????
     *
     * @return
     */
    @RequestMapping(value = "/generateUserId", method = RequestMethod.GET)
    public Result<String> generateUserId() {
        Result<String> result = new Result<>();
        System.out.println("????????????,????????????ID==============================");
        String userId = UUID.randomUUID().toString().replace("-", "");
        result.setSuccess(true);
        result.setResult(userId);
        return result;
    }

    /**
     * ????????????id??????????????????
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryUserByDepId", method = RequestMethod.GET)
    public Result<List<SysUser>> queryUserByDepId(@RequestParam(name = "id", required = true) String id,@RequestParam(name="realname",required=false) String realname) {
        Result<List<SysUser>> result = new Result<>();
        //List<SysUser> userList = sysUserDepartService.queryUserByDepId(id);
        SysDepart sysDepart = sysDepartService.getById(id);
        List<SysUser> userList = sysUserDepartService.queryUserByDepCode(sysDepart.getOrgCode(),realname);

        //?????????????????????????????????
        //step.1 ?????????????????? useids
        //step.2 ?????? useids?????????????????????????????????????????????
        List<String> userIds = userList.stream().map(SysUser::getId).collect(Collectors.toList());
        if(userIds!=null && userIds.size()>0){
            Map<String,String>  useDepNames = sysUserService.getDepNamesByUserIds(userIds);
            userList.forEach(item->{
                //TODO ??????????????????????????????????????????
                item.setOrgCodeTxt(useDepNames.get(item.getId()));
            });
        }

        try {
            result.setSuccess(true);
            result.setResult(userList);
            return result;
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
            result.setSuccess(false);
            return result;
        }
    }

    /**
     * ?????????????????? ??????  ???????????????????????????????????????
     * @param departId
     * @param username
     * @return
     */
    @RequestMapping(value = "/newQueryUserComponentData", method = RequestMethod.GET)
    public Result<IPage<SysUser>> newQueryUserComponentData(
            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
            @RequestParam(name = "departId", required = false) String departId,
            @RequestParam(name="realname",required=false) String realname,
            @RequestParam(name="username",required=false) String username )
    {

            IPage<SysUser> pageList = sysUserDepartService.newqueryDepartUserPageList(departId, username, realname, pageSize, pageNo);

            //IPage<SysUser> pageList = sysUserDepartService.queryDepartUserPageList(departId, username, realname, pageSize, pageNo);

        return Result.OK(pageList);

    }
    @RequestMapping(value = "/queryUserComponentData", method = RequestMethod.GET)
    public Result<IPage<SysUser>> queryUserComponentData(
            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
            @RequestParam(name = "departId", required = false) String departId,
            @RequestParam(name="realname",required=false) String realname,
            @RequestParam(name="username",required=false) String username )
    {

        //IPage<SysUser> pageList = sysUserDepartService.newqueryDepartUserPageList(departId, username, realname, pageSize, pageNo);

        IPage<SysUser> pageList = sysUserDepartService.queryDepartUserPageList(departId, username, realname, pageSize, pageNo);

        return Result.OK(pageList);

    }

    @RequestMapping(value = "/queryVillageComponentData", method = RequestMethod.GET)
    public Result<IPage<SysUser>> queryVillageComponentData(
            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
            @RequestParam(name = "departId", required = false) String departId,
            @RequestParam(name="realname",required=false) String realname,
            @RequestParam(name="username",required=false) String username )
    {

        //IPage<SysUser> pageList = sysUserDepartService.newqueryDepartUserPageList(departId, username, realname, pageSize, pageNo);

        IPage<SysUser> pageList = sysUserDepartService.queryDepartVillagePageList(departId, username, realname, pageSize, pageNo);

        return Result.OK(pageList);

    }

    @RequestMapping(value = "/queryUserByDeptIdComponentData", method = RequestMethod.GET)
    public Result<IPage<SysUser>> queryUserByDeptIdComponentData(
            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
            @RequestParam(name = "departId") String departId,
            @RequestParam(name="realname",required=false) String realname,
            @RequestParam(name="username",required=false) String username ) {
        if(oConvertUtils.isEmpty(departId)){
            return Result.error("???????????????????????????ID");
        }
        IPage<SysUser> pageList = sysUserDepartService.queryRealDepartUserPageList(departId, username, realname, pageSize, pageNo);
        return Result.OK(pageList);
    }

    /**
     * ????????????????????????
     * @param departId
     * @return
     */
    @RequestMapping(value = "/countByPoliticalStatus", method = RequestMethod.GET)
    public Result<JSONObject> queryPageListByPoliticalStatus(@RequestParam(name = "departId", required = true) String departId) {
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();


        // ???????????????????????????????????????????????????????????????
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("depart_id", departId).eq("political_status", 1).eq("del_flag", 0);
        long count = sysUserService.count(queryWrapper);

        obj.put("count", count);
        result.setResult(obj);

        return result;
    }

    /**
     * ??????excel
     *
     * @param req
     * @param sysUser
     */
    @RequestMapping(value = "/exportXlsVillage")
    public ModelAndView exportXlsVillage(HttpServletRequest req,
                                  HttpServletResponse response,SysUser sysUser)throws Exception {
        // ???????????????????????????????????????????????????????????????
        LoginUser currentUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String username = currentUser.getUsername();

        // ??????????????????
        List<String> role = sysBaseAPI.getRolesByUsername(username);

        List<SysUser> queryList = new ArrayList<SysUser>();

        // ????????????????????????????????????????????????????????????
//        if(role.contains("CommonUser")) {
//            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("create_by",username);
//            queryList = sysUserService.list(queryWrapper);
//        }
//        else {
            // 1. ?????????????????? ???**??????
            //String rule = "in";
            // 2. ????????????
            //String field = "departId";

            // ???????????????ID
            //String childrenIdString = commonService.getChildrenIdStringByOrgCode(currentUser.getOrgCode());

           // HashMap<String, String[]> map = new HashMap<>(req.getParameterMap());
            // ????????????????????????superQueryParams
            //List<String> paramsList = ParamsUtil.getSuperQueryParams(req.getParameterMap());

            // ?????????????????????????????????????????????
//            paramsList.add("%5B%7B%22rule%22:%22" + rule + "%22,%22type%22:%22string%22,%22dictCode%22:%22%22,%22val%22:%22"
//                    + childrenIdString
//                    + "%22,%22field%22:%22" + field + "%22%7D%5D");
//            String[] params = new String[paramsList.size()];
//            paramsList.toArray(params);
//            map.put("superQueryParams", params);
//            params = new String[]{"and"};
//            map.put("superQueryMatchType", params);
            //QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(sysUser, map);
//            String departId = currentUser.getDepartId();
            QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
            sysUserQueryWrapper.eq("people_type",2);
            queryList = sysUserService.list(sysUserQueryWrapper);
//        }


        // Step.1 ??????????????????????????????

        //Step.2 ??????????????????
        // ??????????????????
        String selections = req.getParameter("selections");
        List<SysUser> sysUserList = new ArrayList<SysUser>();
        if(oConvertUtils.isEmpty(selections)) {
            sysUserList = queryList;
        }else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            sysUserList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 ??????pageList
        //List<SysUser> pageList = new ArrayList<SysUser>();
        List<VillageUser> voPageList = new ArrayList<>();
        for (SysUser main : sysUserList) {
            //SysUser user = new SysUser();
            VillageUser vo = new VillageUser();
            //BeanUtils.copyProperties(main, user);
            BeanUtils.copyProperties(main, vo);
            //pageList.add(user);
            voPageList.add(vo);
        }

        // Step.4 AutoPoi ??????Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "????????????");
        mv.addObject(NormalExcelConstants.CLASS, VillageUser.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("????????????", "?????????:"+currentUser.getRealname(), "?????????"));
        mv.addObject(NormalExcelConstants.DATA_LIST, voPageList);

        // List??????????????????????????????????????????
        List<VillageUser> newPageList = ObjectUtil.cloneByStream(voPageList);

        baseCommon_Service.addExportLog(mv.getModel(), "??????", req, response);

        mv.addObject(NormalExcelConstants.DATA_LIST, newPageList);

        return mv;
       /* // Step.1 ??????????????????

        //Step.2 AutoPoi ??????Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        //update-begin--Author:kangxiaolin  Date:20180825 for???[03]?????????????????????????????????????????????????????????--------------------
        String selections = req.getParameter("selections");
       if(!oConvertUtils.isEmpty(selections)){
           queryWrapper.in("id",selections.split(","));
       }
        //update-end--Author:kangxiaolin  Date:20180825 for???[03]?????????????????????????????????????????????????????????----------------------
        List<SysUser> pageList = sysUserService.list(queryWrapper);

        //??????????????????
        mv.addObject(NormalExcelConstants.FILE_NAME, "????????????");
        mv.addObject(NormalExcelConstants.CLASS, SysUser.class);
		LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ExportParams exportParams = new ExportParams("??????????????????", "?????????:"+user.getRealname(), "????????????");
        exportParams.setImageBasePath(upLoadPath);
        mv.addObject(NormalExcelConstants.PARAMS, exportParams);
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;*/
    }

    /**
     * ??????excel
     *
     * @param req
     * @param sysUser
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest req,
                                  HttpServletResponse response,SysUser sysUser)throws Exception {
        // ???????????????????????????????????????????????????????????????
        LoginUser currentUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String username = currentUser.getUsername();

        // ??????????????????
        List<String> role = sysBaseAPI.getRolesByUsername(username);

        List<SysUser> queryList = new ArrayList<SysUser>();
//        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(sysUser, req.getParameterMap());
        queryWrapper.eq("del_flag",0);
        queryWrapper.eq("people_type","1");
        // ????????????????????????????????????????????????????????????
         if(role.contains("CCDIAdmin")){
            queryList = sysUserService.list(queryWrapper);
        }
        else if(role.contains("admin")&&!role.contains("CCDIAdmin")) {
            // 1. ?????????????????? ???**??????
            //String rule = "in";
            // 2. ????????????
            //String field = "departId";

            // ???????????????ID
            //String childrenIdString = commonService.getChildrenIdStringByOrgCode(currentUser.getOrgCode());

            // HashMap<String, String[]> map = new HashMap<>(req.getParameterMap());
            // ????????????????????????superQueryParams
            //List<String> paramsList = ParamsUtil.getSuperQueryParams(req.getParameterMap());

            // ?????????????????????????????????????????????
//            paramsList.add("%5B%7B%22rule%22:%22" + rule + "%22,%22type%22:%22string%22,%22dictCode%22:%22%22,%22val%22:%22"
//                    + childrenIdString
//                    + "%22,%22field%22:%22" + field + "%22%7D%5D");
//            String[] params = new String[paramsList.size()];
//            paramsList.toArray(params);
//            map.put("superQueryParams", params);
//            params = new String[]{"and"};
//            map.put("superQueryMatchType", params);
            //QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(sysUser, map);
            String departId = currentUser.getDepartId();
//            QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
//            sysUserQueryWrapper.eq("depart_id",departId);
             queryWrapper.eq("depart_id",departId);
            queryList = sysUserService.list(queryWrapper);
        }


        // Step.1 ??????????????????????????????

        //Step.2 ??????????????????
        // ??????????????????
        String selections = req.getParameter("selections");
        List<SysUser> sysUserList = new ArrayList<SysUser>();
        if(oConvertUtils.isEmpty(selections)) {
            sysUserList = queryList;
        }else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            sysUserList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 ??????pageList
        //List<SysUser> pageList = new ArrayList<SysUser>();
        List<SysUserVo> voPageList = new ArrayList<>();
        for (SysUser main : sysUserList) {
            //SysUser user = new SysUser();
            SysUserVo vo = new SysUserVo();
            //BeanUtils.copyProperties(main, user);
            BeanUtils.copyProperties(main, vo);
            //pageList.add(user);
            voPageList.add(vo);
        }

        // Step.4 AutoPoi ??????Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "????????????");
        mv.addObject(NormalExcelConstants.CLASS, SysUserVo.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("????????????", "?????????:"+currentUser.getRealname(), "?????????"));
        mv.addObject(NormalExcelConstants.DATA_LIST, voPageList);

        // List??????????????????????????????????????????
        List<SysUserVo> newPageList = ObjectUtil.cloneByStream(voPageList);

        baseCommon_Service.addExportLog(mv.getModel(), "??????", req, response);

        mv.addObject(NormalExcelConstants.DATA_LIST, newPageList);

        return mv;
       /* // Step.1 ??????????????????

        //Step.2 AutoPoi ??????Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        //update-begin--Author:kangxiaolin  Date:20180825 for???[03]?????????????????????????????????????????????????????????--------------------
        String selections = req.getParameter("selections");
       if(!oConvertUtils.isEmpty(selections)){
           queryWrapper.in("id",selections.split(","));
       }
        //update-end--Author:kangxiaolin  Date:20180825 for???[03]?????????????????????????????????????????????????????????----------------------
        List<SysUser> pageList = sysUserService.list(queryWrapper);

        //??????????????????
        mv.addObject(NormalExcelConstants.FILE_NAME, "????????????");
        mv.addObject(NormalExcelConstants.CLASS, SysUser.class);
		LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ExportParams exportParams = new ExportParams("??????????????????", "?????????:"+user.getRealname(), "????????????");
        exportParams.setImageBasePath(upLoadPath);
        mv.addObject(NormalExcelConstants.PARAMS, exportParams);
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;*/
    }
    /**
     * ??????excel????????????
     *
     * @param request
     * @param response
     * @return
     */
    //@RequiresRoles({"admin"})
    //@RequiresPermissions("user:import")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response)throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        // ????????????
        List<String> errorMessage = new ArrayList<>();
        int successLines = 0, errorLines = 0;
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// ????????????????????????
            ImportParams params = new ImportParams();
            params.setTitleRows(1);//?????????????????????????????????????????????????????????????????????????????????????????????????????????
            params.setHeadRows(2);//????????????????????????????????????????????????????????????????????????
            params.setNeedSave(true);
            try {
                List<SysUser> listSysUsers = ExcelImportUtil.importExcel(file.getInputStream(), SysUser.class, params);
                for (int i = 0; i < listSysUsers.size(); i++) {
                    SysUser sysUserExcel = listSysUsers.get(i);
                    //????????????
                    sysUserExcel.setStatus(1);
                    //??????????????????
                    if (StringUtils.isBlank(sysUserExcel.getUsername())) {
                        //????????????????????????
                        sysUserExcel.setUsername(sysUserExcel.getPhone());
                    }
                    //??????????????????
                    if (StringUtils.isBlank(sysUserExcel.getPassword())) {
                        // ??????????????? ???123456???
                        sysUserExcel.setPassword("123456");
                    }
                    // ??????????????????
                    String salt = oConvertUtils.randomGen(8);
                    sysUserExcel.setSalt(salt);
                    String passwordEncode = PasswordUtil.encrypt(sysUserExcel.getUsername(), sysUserExcel.getPassword(), salt);
                    sysUserExcel.setPassword(passwordEncode);
                    //??????ID,?????????????????????????????????

                    String orgCode = sysUserExcel.getOrgCode();
//                    List<String> orgCodeList = Arrays.asList(orgCode.split(","));
//                    List<String> deptIdList = new ArrayList<>();
//                    for (int index = 0; index<orgCodeList.size();index++){
//                        deptIdList.add(commonService.getDepartIdByOrgCode(orgCodeList.get(index)));
//                    }
//                    String deptId = String.join(",", deptIdList);
                    String deptId = commonService.getDepartIdByOrgCode(orgCode);
                    sysUserExcel.setDepartId(deptId);
                    if(sysUserExcel.getUserIdentity() == 2){
                        sysUserExcel.setDepartIds(deptId);
                    }
                    Integer yn = sysUserExcel.getUserIdentity();
                    //??????????????????????????????????????????
                    String role = "1465163864583323650";
                    //System.out.println("ynynynynyn"+yn);
                    if(yn == 2){
                        role = "f6817f48af4fb3af11b9e8bf182f618b";
                    }
                    sysUserExcel.setPeopleType("1");
                    sysUserExcel.setCreateTime(new Date());
                    sysUserExcel.setDelFlag(CommonConstant.DEL_FLAG_0);
                    System.out.println("sysUserExcel"+sysUserExcel);
                    try {
                        sysUserService.saveUser(sysUserExcel,role,deptId);
                        //sysUserService.save(sysUserExcel);
                        successLines++;
                    } catch (Exception e) {
                        errorLines++;
                        String message = e.getMessage().toLowerCase();
                        int lineNumber = i + 1;
                        // ?????????????????????????????????
//                        if (message.contains(CommonConstant.SQL_INDEX_UNIQ_SYS_USER_USERNAME)) {
//                            errorMessage.add("??? " + lineNumber + " ?????????????????????????????????????????????");
//                        } else if (message.contains(CommonConstant.SQL_INDEX_UNIQ_SYS_USER_WORK_NO)) {
//                            errorMessage.add("??? " + lineNumber + " ??????????????????????????????????????????");
//                        } else if (message.contains(CommonConstant.SQL_INDEX_UNIQ_SYS_USER_PHONE)) {
//                            errorMessage.add("??? " + lineNumber + " ?????????????????????????????????????????????");
//                        } else if (message.contains(CommonConstant.SQL_INDEX_UNIQ_SYS_USER_EMAIL)) {
//                            errorMessage.add("??? " + lineNumber + " ????????????????????????????????????????????????");
//                        }
                        if (message.contains(CommonConstant.SQL_INDEX_UNIQ_SYS_USER_PHONE)) {
                            errorMessage.add("??? " + lineNumber + " ?????????????????????????????????????????????");
                        } else if(message.contains(CommonConstant.SQL_INDEX_UNIQ_SYS_IDNUMBER)){
                            errorMessage.add("??? " + lineNumber + " ????????????????????????????????????????????????");
                        }
                        else {
                            errorMessage.add("??? " + lineNumber + " ?????????????????????????????????");
                            log.error(e.getMessage(), e);
                        }
                    }
                    // ????????????????????????????????????????????????
//                    String departIds = sysUserExcel.getDepartIds();
//                    if (StringUtils.isNotBlank(departIds)) {
//                        String userId = sysUserExcel.getId();
//                        String[] departIdArray = departIds.split(",");
//                        List<SysUserDepart> userDepartList = new ArrayList<>(departIdArray.length);
//                        for (String departId : departIdArray) {
//                            userDepartList.add(new SysUserDepart(userId, departId));
//                        }
//                        sysUserDepartService.saveBatch(userDepartList);
//                    }

                }
            } catch (Exception e) {
                errorMessage.add("???????????????" + e.getMessage());
                log.error(e.getMessage(), e);
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                	log.error(e.getMessage(), e);
                }
            }
        }
        return ImportExcelUtil.imporReturnRes(errorLines,successLines,errorMessage);
    }

    /**
	 * @???????????????id ????????????
	 * @param userIds
	 * @return
	 */
	@RequestMapping(value = "/queryByIds", method = RequestMethod.GET)
	public Result<Collection<SysUser>> queryByIds(@RequestParam String userIds) {
		Result<Collection<SysUser>> result = new Result<>();
		String[] userId = userIds.split(",");
		Collection<String> idList = Arrays.asList(userId);
		Collection<SysUser> userRole = sysUserService.listByIds(idList);
		result.setSuccess(true);
		result.setResult(userRole);
		return result;
	}

	/**
	 * ????????????????????????
	 */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/updatePassword", method = RequestMethod.PUT)
	public Result<?> updatePassword(@RequestBody JSONObject json) {
		String username = json.getString("username");
		String oldpassword = json.getString("oldpassword");
		String password = json.getString("password");
		String confirmpassword = json.getString("confirmpassword");
        LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        if(!sysUser.getUsername().equals(username)){
            return Result.error("?????????????????????????????????");
        }
		SysUser user = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
		if(user==null) {
			return Result.error("??????????????????");
		}
		return sysUserService.resetPassword(username,oldpassword,password,confirmpassword);
	}

    @RequestMapping(value = "/updatePhone", method = RequestMethod.PUT)
    public Result<?> updatPhone(@RequestBody JSONObject json) {
        Result<JSONObject> result = new Result<JSONObject>();
        String username = json.getString("username");
        String phone = json.getString("phone");
        LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        String captcha = json.getString("captcha");
        Object code = redisUtil.get(phone);
        if(!sysUser.getUsername().equals(username)){
            return Result.error("???????????????????????????????????????");
        }
        SysUser user = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        if(user==null) {
            return Result.error("??????????????????");
        }
        if (!captcha.equals(code)) {
            result.setMessage("???????????????");
            result.setSuccess(false);
            return result;
        }
        // ??????????????????????????????
        redisUtil.set(phone, captcha,600);
        // ??????????????????
        user.setPhone(phone);
        sysUserService.updateById(user);
        return Result.ok("????????????????????????!");

    }

    @RequestMapping(value = "/userRoleList", method = RequestMethod.GET)
    public Result<IPage<SysUser>> userRoleList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<SysUser>> result = new Result<IPage<SysUser>>();
        Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
        String roleId = req.getParameter("roleId");
        if (oConvertUtils.isEmpty(roleId)) {
            result.setCode(500);
            result.setSuccess(false);
            result.setMessage("??????????????????! ");
            return result;
        }
        String username = req.getParameter("username");
        List<String> departIdList = new ArrayList<>();
        String departIds = req.getParameter("departIds");
        SqlInjectionUtil.filterContent(new String[]{roleId, username, departIds});
        if (oConvertUtils.isNotEmpty(departIds)) {
            departIdList = Arrays.asList(departIds.split(","));
        }
        IPage<SysUser> pageList = sysUserService.getUserByRoleId(page,roleId,username, departIdList);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * ???????????????????????????
     *
     * @param
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/addSysUserRole", method = RequestMethod.POST)
    public Result<String> addSysUserRole(@RequestBody SysUserRoleVO sysUserRoleVO) {
        Result<String> result = new Result<String>();
        try {
            String sysRoleId = sysUserRoleVO.getRoleId();
            for(String sysUserId:sysUserRoleVO.getUserIdList()) {
                SysUserRole sysUserRole = new SysUserRole(sysUserId,sysRoleId);
                QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<SysUserRole>();
                queryWrapper.eq("role_id", sysRoleId).eq("user_id",sysUserId);
                SysUserRole one = sysUserRoleService.getOne(queryWrapper);
                if(one==null){
                    sysUserRoleService.save(sysUserRole);
                }

            }
            result.setMessage("????????????!");
            result.setSuccess(true);
            return result;
        }catch(Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("?????????: " + e.getMessage());
            return result;
        }
    }
    /**
     *   ?????????????????????????????????
     * @param
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/deleteUserRole", method = RequestMethod.DELETE)
    public Result<SysUserRole> deleteUserRole(@RequestParam(name="roleId") String roleId,
                                                    @RequestParam(name="userId",required=true) String userId
    ) {
        Result<SysUserRole> result = new Result<SysUserRole>();
        try {
            QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<SysUserRole>();
            queryWrapper.eq("role_id", roleId).eq("user_id",userId);
            sysUserRoleService.remove(queryWrapper);
            result.success("????????????!");
        }catch(Exception e) {
            log.error(e.getMessage(), e);
            result.error500("???????????????");
        }
        return result;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/deleteUserRoleBatch", method = RequestMethod.DELETE)
    public Result<SysUserRole> deleteUserRoleBatch(
            @RequestParam(name="roleId") String roleId,
            @RequestParam(name="userIds",required=true) String userIds) {
        Result<SysUserRole> result = new Result<SysUserRole>();
        try {
            QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<SysUserRole>();
            queryWrapper.eq("role_id", roleId).in("user_id",Arrays.asList(userIds.split(",")));
            sysUserRoleService.remove(queryWrapper);
            result.success("????????????!");
        }catch(Exception e) {
            log.error(e.getMessage(), e);
            result.error500("???????????????");
        }
        return result;
    }

    /**
     * ??????????????????
     */
    @RequestMapping(value = "/departUserList", method = RequestMethod.GET)
    public Result<IPage<SysUser>> departUserList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<SysUser>> result = new Result<IPage<SysUser>>();
        Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
        String depId = req.getParameter("depId");
        String username = req.getParameter("username");
        //????????????ID??????,??????????????????????????????IDS
        List<String> subDepids = new ArrayList<>();
        //??????id?????????????????????????????????????????????
        if(oConvertUtils.isEmpty(depId)){
            LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            int userIdentity = user.getUserIdentity() != null?user.getUserIdentity():CommonConstant.USER_IDENTITY_1;
            if(oConvertUtils.isNotEmpty(userIdentity) && userIdentity == CommonConstant.USER_IDENTITY_2 ){
                subDepids = sysDepartService.getMySubDepIdsByDepId(user.getDepartIds());
            }
        }else{
            subDepids = sysDepartService.getSubDepIdsByDepId(depId);
        }
        if(subDepids != null && subDepids.size()>0){
            IPage<SysUser> pageList = sysUserService.getUserByDepIds(page,subDepids,username);
            //?????????????????????????????????
            //step.1 ?????????????????? useids
            //step.2 ?????? useids?????????????????????????????????????????????
            List<String> userIds = pageList.getRecords().stream().map(SysUser::getId).collect(Collectors.toList());
            if(userIds!=null && userIds.size()>0){
                Map<String, String> useDepNames = sysUserService.getDepNamesByUserIds(userIds);
                pageList.getRecords().forEach(item -> {
                    //?????????????????????????????????
                    item.setOrgCode(useDepNames.get(item.getId()));
                });
            }
            result.setSuccess(true);
            result.setResult(pageList);
        }else{
            result.setSuccess(true);
            result.setResult(null);
        }
        return result;
    }


    /**
     * ?????? orgCode ??????????????????????????????????????????
     * ?????????????????????????????????????????????????????????????????????????????????????????????
     */
    @GetMapping("/queryByOrgCode")
    public Result<?> queryByDepartId(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "orgCode") String orgCode,
            SysUser userParams
    ) {
        IPage<SysUserSysDepartModel> pageList = sysUserService.queryUserByOrgCode(orgCode, userParams, new Page(pageNo, pageSize));
        return Result.ok(pageList);
    }

    /**
     * ?????? orgCode ??????????????????????????????????????????
     * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    @GetMapping("/queryByOrgCodeForAddressList")
    public Result<?> queryByOrgCodeForAddressList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "orgCode",required = false) String orgCode,
            SysUser userParams
    ) {
        IPage page = new Page(pageNo, pageSize);
        IPage<SysUserSysDepartModel> pageList = sysUserService.queryUserByOrgCode(orgCode, userParams, page);
        List<SysUserSysDepartModel> list = pageList.getRecords();

        // ???????????????????????? user, key = userId
        Map<String, JSONObject> hasUser = new HashMap<>(list.size());

        JSONArray resultJson = new JSONArray(list.size());

        for (SysUserSysDepartModel item : list) {
            String userId = item.getId();
            // userId
            JSONObject getModel = hasUser.get(userId);
            // ????????????????????????????????????????????????
            if (getModel != null) {
                String departName = getModel.get("departName").toString();
                getModel.put("departName", (departName + " | " + item.getDepartName()));
            } else {
                // ????????????????????????json???????????????????????????????????? json ???
                JSONObject json = JSON.parseObject(JSON.toJSONString(item));
                json.remove("id");
                json.put("userId", userId);
                json.put("departId", item.getDepartId());
                json.put("departName", item.getDepartName());
//                json.put("avatar", item.getSysUser().getAvatar());
                resultJson.add(json);
                hasUser.put(userId, json);
            }
        }

        IPage<JSONObject> result = new Page<>(pageNo, pageSize, pageList.getTotal());
        result.setRecords(resultJson.toJavaList(JSONObject.class));
        return Result.ok(result);
    }

    /**
     * ????????????????????????????????????
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/editSysDepartWithUser", method = RequestMethod.POST)
    public Result<String> editSysDepartWithUser(@RequestBody SysDepartUsersVO sysDepartUsersVO) {
        Result<String> result = new Result<String>();
        try {
            String sysDepId = sysDepartUsersVO.getDepId();
            for(String sysUserId:sysDepartUsersVO.getUserIdList()) {
                SysUserDepart sysUserDepart = new SysUserDepart(null,sysUserId,sysDepId);
                QueryWrapper<SysUserDepart> queryWrapper = new QueryWrapper<SysUserDepart>();
                queryWrapper.eq("dep_id", sysDepId).eq("user_id",sysUserId);
                SysUserDepart one = sysUserDepartService.getOne(queryWrapper);
                if(one==null){
                    sysUserDepartService.save(sysUserDepart);
                }
            }
            result.setMessage("????????????!");
            result.setSuccess(true);
            return result;
        }catch(Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("?????????: " + e.getMessage());
            return result;
        }
    }

    /**
     *   ?????????????????????????????????
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/deleteUserInDepart", method = RequestMethod.DELETE)
    public Result<SysUserDepart> deleteUserInDepart(@RequestParam(name="depId") String depId,
                                                    @RequestParam(name="userId",required=true) String userId
    ) {
        Result<SysUserDepart> result = new Result<SysUserDepart>();
        try {
            QueryWrapper<SysUserDepart> queryWrapper = new QueryWrapper<SysUserDepart>();
            queryWrapper.eq("dep_id", depId).eq("user_id",userId);
            boolean b = sysUserDepartService.remove(queryWrapper);
            if(b){
                List<SysDepartRole> sysDepartRoleList = departRoleService.list(new QueryWrapper<SysDepartRole>().eq("depart_id",depId));
                List<String> roleIds = sysDepartRoleList.stream().map(SysDepartRole::getId).collect(Collectors.toList());
                if(roleIds != null && roleIds.size()>0){
                    QueryWrapper<SysDepartRoleUser> query = new QueryWrapper<>();
                    query.eq("user_id",userId).in("drole_id",roleIds);
                    departRoleUserService.remove(query);
                }
                result.success("????????????!");
            }else{
                result.error500("??????????????????????????????????????????!");
            }
        }catch(Exception e) {
            log.error(e.getMessage(), e);
            result.error500("???????????????");
        }
        return result;
    }

    /**
     * ???????????????????????????????????????
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/deleteUserInDepartBatch", method = RequestMethod.DELETE)
    public Result<SysUserDepart> deleteUserInDepartBatch(
            @RequestParam(name="depId") String depId,
            @RequestParam(name="userIds",required=true) String userIds) {
        Result<SysUserDepart> result = new Result<SysUserDepart>();
        try {
            QueryWrapper<SysUserDepart> queryWrapper = new QueryWrapper<SysUserDepart>();
            queryWrapper.eq("dep_id", depId).in("user_id",Arrays.asList(userIds.split(",")));
            boolean b = sysUserDepartService.remove(queryWrapper);
            if(b){
                departRoleUserService.removeDeptRoleUser(Arrays.asList(userIds.split(",")),depId);
            }
            result.success("????????????!");
        }catch(Exception e) {
            log.error(e.getMessage(), e);
            result.error500("???????????????");
        }
        return result;
    }
    
    /**
         *  ?????????????????????????????????/??????????????????
     * @return
     */
    @RequestMapping(value = "/getCurrentUserDeparts", method = RequestMethod.GET)
    public Result<Map<String,Object>> getCurrentUserDeparts() {
        Result<Map<String,Object>> result = new Result<Map<String,Object>>();
        try {
        	LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
            List<SysDepart> list = this.sysDepartService.queryUserDeparts(sysUser.getId());
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("list", list);
            map.put("orgCode", sysUser.getOrgCode());
            result.setSuccess(true);
            result.setResult(map);
        }catch(Exception e) {
            log.error(e.getMessage(), e);
            result.error500("???????????????");
        }
        return result;
    }

    


	/**
	 * ??????????????????
	 * 
	 * @param jsonObject
	 * @param user
	 * @return
	 */
	@PostMapping("/register")
	public Result<JSONObject> userRegister(@RequestBody JSONObject jsonObject, SysUser user) {
		Result<JSONObject> result = new Result<JSONObject>();
		String phone = jsonObject.getString("phone");
		String smscode = jsonObject.getString("smscode");
		Object code = redisUtil.get(phone);
		String username = jsonObject.getString("username");
		//???????????????????????????????????????????????????
		if(oConvertUtils.isEmpty(username)){
            username = phone;
        }
        //?????????????????????????????????????????????
		String password = jsonObject.getString("password");
		if(oConvertUtils.isEmpty(password)){
            password = RandomUtil.randomString(8);
        }
		String email = jsonObject.getString("email");
		SysUser sysUser1 = sysUserService.getUserByName(username);
		if (sysUser1 != null) {
			result.setMessage("??????????????????");
			result.setSuccess(false);
			return result;
		}
		SysUser sysUser2 = sysUserService.getUserByPhone(phone);
		if (sysUser2 != null) {
			result.setMessage("?????????????????????");
			result.setSuccess(false);
			return result;
		}

		if(oConvertUtils.isNotEmpty(email)){
            SysUser sysUser3 = sysUserService.getUserByEmail(email);
            if (sysUser3 != null) {
                result.setMessage("??????????????????");
                result.setSuccess(false);
                return result;
            }
        }
        if(null == code){
            result.setMessage("???????????????????????????????????????");
            result.setSuccess(false);
            return result;
        }
		if (!smscode.equals(code.toString())) {
			result.setMessage("?????????????????????");
			result.setSuccess(false);
			return result;
		}

		try {
			user.setCreateTime(new Date());// ??????????????????
			String salt = oConvertUtils.randomGen(8);
			String passwordEncode = PasswordUtil.encrypt(username, password, salt);
			user.setSalt(salt);
			user.setUsername(username);
			user.setRealname(username);
			user.setPassword(passwordEncode);
			user.setEmail(email);
			user.setPhone(phone);
			user.setStatus(CommonConstant.USER_UNFREEZE);
			user.setDelFlag(CommonConstant.DEL_FLAG_0);
			user.setActivitiSync(CommonConstant.ACT_SYNC_0);
			sysUserService.addUserWithRole(user,"ee8626f80f7c2619917b6236f3a7f02b");//?????????????????? test
			result.success("????????????");
		} catch (Exception e) {
			result.error500("????????????");
		}
		return result;
	}

//	/**
//	 * ?????????????????????????????????????????????
//	 * @param
//	 * @return
//	 */
//	@GetMapping("/querySysUser")
//	public Result<Map<String, Object>> querySysUser(SysUser sysUser) {
//		String phone = sysUser.getPhone();
//		String username = sysUser.getUsername();
//		Result<Map<String, Object>> result = new Result<Map<String, Object>>();
//		Map<String, Object> map = new HashMap<String, Object>();
//		if (oConvertUtils.isNotEmpty(phone)) {
//			SysUser user = sysUserService.getUserByPhone(phone);
//			if(user!=null) {
//				map.put("username",user.getUsername());
//				map.put("phone",user.getPhone());
//				result.setSuccess(true);
//				result.setResult(map);
//				return result;
//			}
//		}
//		if (oConvertUtils.isNotEmpty(username)) {
//			SysUser user = sysUserService.getUserByName(username);
//			if(user!=null) {
//				map.put("username",user.getUsername());
//				map.put("phone",user.getPhone());
//				result.setSuccess(true);
//				result.setResult(map);
//				return result;
//			}
//		}
//		result.setSuccess(false);
//		result.setMessage("????????????");
//		return result;
//	}

	/**
	 * ?????????????????????
	 */
	@PostMapping("/phoneVerification")
	public Result<Map<String,String>> phoneVerification(@RequestBody JSONObject jsonObject) {
		Result<Map<String,String>> result = new Result<Map<String,String>>();
		String phone = jsonObject.getString("phone");
		String smscode = jsonObject.getString("smscode");
		Object code = redisUtil.get(phone);
		if (!smscode.equals(code)) {
			result.setMessage("?????????????????????");
			result.setSuccess(false);
			return result;
		}
		//??????????????????
		redisUtil.set(phone, smscode,600);
		//?????????????????????
		LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<>();
        query.eq(SysUser::getPhone,phone);
        SysUser user = sysUserService.getOne(query);
        Map<String,String> map = new HashMap<>();
        map.put("smscode",smscode);
        map.put("username",user.getUsername());
        result.setResult(map);
		result.setSuccess(true);
		return result;
	}
	
	/**
	 * ??????????????????
	 */
	@GetMapping("/passwordChange")
	public Result<SysUser> passwordChange(@RequestParam(name="username")String username,
										  @RequestParam(name="password")String password,
			                              @RequestParam(name="smscode")String smscode,
			                              @RequestParam(name="phone") String phone) {
        Result<SysUser> result = new Result<SysUser>();
        if(oConvertUtils.isEmpty(username) || oConvertUtils.isEmpty(password) || oConvertUtils.isEmpty(smscode)  || oConvertUtils.isEmpty(phone) ) {
            result.setMessage("?????????????????????");
            result.setSuccess(false);
            return result;
        }

        SysUser sysUser=new SysUser();
        Object object= redisUtil.get(phone);
        if(null==object) {
        	result.setMessage("????????????????????????");
            result.setSuccess(false);
            return result;
        }
        if(!smscode.equals(object.toString())) {
        	result.setMessage("???????????????????????????");
            result.setSuccess(false);
            return result;
        }
        sysUser = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername,username).eq(SysUser::getPhone,phone));
        if (sysUser == null) {
            result.setMessage("??????????????????");
            result.setSuccess(false);
            return result;
        } else {
            String salt = oConvertUtils.randomGen(8);
            sysUser.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), password, salt);
            sysUser.setPassword(passwordEncode);
            this.sysUserService.updateById(sysUser);
            result.setSuccess(true);
            result.setMessage("?????????????????????");
            return result;
        }
    }
	

	/**
	 * ??????TOKEN???????????????????????????????????????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	@GetMapping("/getUserSectionInfoByToken")
	public Result<?> getUserSectionInfoByToken(HttpServletRequest request, @RequestParam(name = "token", required = false) String token) {
		try {
			String username = null;
			// ??????????????????token?????????header?????????token?????????????????????
			if (oConvertUtils.isEmpty(token)) {
				 username = JwtUtil.getUserNameByToken(request);
			} else {
				 username = JwtUtil.getUsername(token);				
			}

			log.debug(" ------ ?????????????????????????????????????????????????????? " + username);

			// ?????????????????????????????????
			SysUser sysUser = sysUserService.getUserByName(username);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sysUserId", sysUser.getId());
			map.put("sysUserCode", sysUser.getUsername()); // ??????????????????????????????
			map.put("sysUserName", sysUser.getRealname()); // ??????????????????????????????
			map.put("sysOrgCode", sysUser.getOrgCode()); // ??????????????????????????????

			log.debug(" ------ ?????????????????????????????????????????????????????????????????? " + map);

			return Result.ok(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Result.error(500, "????????????:" + e.getMessage());
		}
	}
	
	/**
	 * ???APP??????????????????????????????  ??????????????????????????? ????????????
	 * @param keyword
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@GetMapping("/appUserList")
	public Result<?> appUserList(@RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "username", required = false) String username,
			@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
            @RequestParam(name = "syncFlow", required = false) String syncFlow) {
		try {
			//TODO ??????????????????????????????mp????????????page???????????? ???????????????????????????
			LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<SysUser>();
			if(oConvertUtils.isNotEmpty(syncFlow)){
                query.eq(SysUser::getActivitiSync, CommonConstant.ACT_SYNC_1);
            }
			query.eq(SysUser::getDelFlag,CommonConstant.DEL_FLAG_0);
			if(oConvertUtils.isNotEmpty(username)){
			    if(username.contains(",")){
                    query.in(SysUser::getUsername,username.split(","));
                }else{
                    query.eq(SysUser::getUsername,username);
                }
            }else{
                query.and(i -> i.like(SysUser::getUsername, keyword).or().like(SysUser::getRealname, keyword));
            }
			Page<SysUser> page = new Page<>(pageNo, pageSize);
			IPage<SysUser> res = this.sysUserService.page(page, query);
			return Result.ok(res);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Result.error(500, "????????????:" + e.getMessage());
		}
		
	}

    /**
     * ????????????????????????????????????????????????
     *
     * @return logicDeletedUserList
     */
    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<SysUser> logicDeletedUserList = sysUserService.queryLogicDeleted();
        if (logicDeletedUserList.size() > 0) {
            // ?????????????????????????????????
            // step.1 ?????????????????? userIds
            List<String> userIds = logicDeletedUserList.stream().map(SysUser::getId).collect(Collectors.toList());
            // step.2 ?????? userIds?????????????????????????????????????????????
            Map<String, String> useDepNames = sysUserService.getDepNamesByUserIds(userIds);
            logicDeletedUserList.forEach(item -> item.setOrgCode(useDepNames.get(item.getId())));
        }
        return Result.ok(logicDeletedUserList);
    }

    /**
     * ??????????????????????????????
     *
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/putRecycleBin", method = RequestMethod.PUT)
    public Result putRecycleBin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String userIds = jsonObject.getString("userIds");
        if (StringUtils.isNotBlank(userIds)) {
            SysUser updateUser = new SysUser();
            updateUser.setUpdateBy(JwtUtil.getUserNameByToken(request));
            updateUser.setUpdateTime(new Date());
            sysUserService.revertLogicDeleted(Arrays.asList(userIds.split(",")), updateUser);
        }
        return Result.ok("????????????");
    }

    /**
     * ??????????????????
     *
     * @param userIds ??????????????????ID?????????id?????????????????????
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
    public Result deleteRecycleBin(@RequestParam("userIds") String userIds) {
        if (StringUtils.isNotBlank(userIds)) {
            sysUserService.removeLogicDeleted(Arrays.asList(userIds.split(",")));
        }
        return Result.ok("????????????");
    }


    /**
     * ???????????????????????????
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/appEdit", method = RequestMethod.PUT)
    public Result<SysUser> appEdit(HttpServletRequest request,@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<SysUser>();
        try {
            String username = JwtUtil.getUserNameByToken(request);
            SysUser sysUser = sysUserService.getUserByName(username);
            baseCommonService.addLog("????????????????????????id??? " +jsonObject.getString("id") ,CommonConstant.LOG_TYPE_2, 2);
            String realname=jsonObject.getString("realname");
            String avatar=jsonObject.getString("avatar");
            String sex=jsonObject.getString("sex");
            String phone=jsonObject.getString("phone");
            String email=jsonObject.getString("email");
            Date birthday=jsonObject.getDate("birthday");
            SysUser userPhone = sysUserService.getUserByPhone(phone);
            if(sysUser==null) {
                result.error500("?????????????????????!");
            }else {
                if(userPhone!=null){
                    String userPhonename = userPhone.getUsername();
                    if(!userPhonename.equals(username)){
                        result.error500("??????????????????!");
                        return result;
                    }
                }
                if(StringUtils.isNotBlank(realname)){
                    sysUser.setRealname(realname);
                }
                if(StringUtils.isNotBlank(avatar)){
                    sysUser.setAvatar(avatar);
                }
                if(StringUtils.isNotBlank(sex)){
                    sysUser.setSex(Integer.parseInt(sex));
                }
                if(StringUtils.isNotBlank(phone)){
                    sysUser.setPhone(phone);
                }
                if(StringUtils.isNotBlank(email)){
                    sysUser.setEmail(email);
                }
                if(null != birthday){
                    sysUser.setBirthday(birthday);
                }
                sysUser.setUpdateTime(new Date());
                sysUserService.updateById(sysUser);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("????????????!");
        }
        return result;
    }
    /**
     * ???????????????????????????
     * @param clientId
     * @return
     */
    @RequestMapping(value = "/saveClientId", method = RequestMethod.GET)
    public Result<SysUser> saveClientId(HttpServletRequest request,@RequestParam("clientId")String clientId) {
        Result<SysUser> result = new Result<SysUser>();
        try {
            String username = JwtUtil.getUserNameByToken(request);
            SysUser sysUser = sysUserService.getUserByName(username);
            if(sysUser==null) {
                result.error500("?????????????????????!");
            }else {
                sysUser.setClientId(clientId);
                sysUserService.updateById(sysUser);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("????????????!");
        }
        return result;
    }
    /**
     * ??????userid???????????????????????????????????????
     *
     * @return Result
     */
    @GetMapping("/queryChildrenByUsername")
    public Result queryChildrenByUsername(@RequestParam("userId") String userId) {
        //??????????????????
        Map<String,Object> map=new HashMap<String,Object>();
        SysUser sysUser = sysUserService.getById(userId);
        String username = sysUser.getUsername();
        Integer identity = sysUser.getUserIdentity();
        map.put("sysUser",sysUser);
        if(identity!=null && identity==2){
            //????????????????????????
            String departIds = sysUser.getDepartIds();
            if(StringUtils.isNotBlank(departIds)){
                List<String> departIdList = Arrays.asList(departIds.split(","));
                List<SysUser> childrenUser = sysUserService.queryByDepIds(departIdList,username);
                map.put("children",childrenUser);
            }
        }
        return Result.ok(map);
    }
    /**
     * ?????????????????????????????????
     * @param departId
     * @return
     */
    @GetMapping("/appQueryByDepartId")
    public Result<List<SysUser>> appQueryByDepartId(@RequestParam(name="departId", required = false) String departId) {
        Result<List<SysUser>> result = new Result<List<SysUser>>();
        List<String> list=new ArrayList<String> ();
        list.add(departId);
        List<SysUser> childrenUser = sysUserService.queryByDepIds(list,null);
        result.setResult(childrenUser);
        return result;
    }
    /**
     * ???????????????????????????(???????????????????????????)
     * @param keyword
     * @return
     */
    @GetMapping("/appQueryUser")
    public Result<List<SysUser>> appQueryUser(@RequestParam(name = "keyword", required = false) String keyword) {
        Result<List<SysUser>> result = new Result<List<SysUser>>();
        LambdaQueryWrapper<SysUser> queryWrapper =new LambdaQueryWrapper<SysUser>();
        //TODO ????????????????????????????????????????????????
        queryWrapper.ne(SysUser::getUsername,"_reserve_user_external");
        if(StringUtils.isNotBlank(keyword)){
            queryWrapper.and(i -> i.like(SysUser::getUsername, keyword).or().like(SysUser::getRealname, keyword));
        }
        List<SysUser> list = sysUserService.list(queryWrapper);
        //?????????????????????????????????
        //step.1 ?????????????????? useids
        //step.2 ?????? useids?????????????????????????????????????????????
        List<String> userIds = list.stream().map(SysUser::getId).collect(Collectors.toList());
        if(userIds!=null && userIds.size()>0){
            Map<String,String>  useDepNames = sysUserService.getDepNamesByUserIds(userIds);
            list.forEach(item->{
                item.setOrgCodeTxt(useDepNames.get(item.getId()));
            });
        }
        result.setResult(list);
        return result;
    }

    /**
     * ??????????????????????????????
     * @param json
     * @return
     */
    @RequestMapping(value = "/updateMobile", method = RequestMethod.PUT)
    public Result<?> changMobile(@RequestBody JSONObject json,HttpServletRequest request) {
        String smscode = json.getString("smscode");
        String phone = json.getString("phone");
        Result<SysUser> result = new Result<SysUser>();
        //?????????????????????
        String username = JwtUtil.getUserNameByToken(request);
        if(oConvertUtils.isEmpty(username) || oConvertUtils.isEmpty(smscode) || oConvertUtils.isEmpty(phone)) {
            result.setMessage("????????????????????????");
            result.setSuccess(false);
            return result;
        }
        Object object= redisUtil.get(phone);
        if(null==object) {
            result.setMessage("????????????????????????");
            result.setSuccess(false);
            return result;
        }
        if(!smscode.equals(object.toString())) {
            result.setMessage("???????????????????????????");
            result.setSuccess(false);
            return result;
        }
        SysUser user = sysUserService.getUserByName(username);
        if(user==null) {
            return Result.error("??????????????????");
        }
        user.setPhone(phone);
        sysUserService.updateById(user);
        return Result.ok("?????????????????????!");
    }


    /**
     * ?????????????????????????????????in?????? ?????????????????? ??????????????????
     * @param sysUser
     * @return
     */
    @GetMapping("/getMultiUser")
    public List<SysUser> getMultiUser(SysUser sysUser){
        QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(sysUser, null);
        List<SysUser> ls = this.sysUserService.list(queryWrapper);
        for(SysUser user: ls){
            user.setPassword(null);
            user.setSalt(null);
        }
        return ls;
    }


    /**
     *  ??????????????????????????????ID???????????????????????????????????????????????????????????????
     * @return
     */
    @RequestMapping(value = "/getCurrentUserDeptWorkMessage", method = RequestMethod.GET)
    public Result<Map<String,Object>> getCurrentUserDeptWorkMessage() {
        Result<Map<String,Object>> result = new Result<Map<String,Object>>();
        try {
            LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
            SysDepart currentUserDepart = this.sysDepartService.queryCurrentUserDepart(sysUser.getId());
            SysDepart currentUserParentDepart = this.sysDepartService.queryDeptByDepartId(currentUserDepart.getBusinessParentId());
            List<SysDepart> currentUserChildrenDeparts = this.sysDepartService.queryWorkChildrenDeparts(currentUserDepart.getId());
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("currentUser", sysUser);
            map.put("currentUserDepart", currentUserDepart);
            map.put("currentUserParentDepart", currentUserParentDepart);
            map.put("currentUserChildrenDeparts", currentUserChildrenDeparts);
            result.setSuccess(true);
            result.setResult(map);
        }catch(Exception e) {
            log.error(e.getMessage(), e);
            result.error500("???????????????");
        }
        return result;
    }

    void SentInfCauseU(String id){
        QueryWrapper<SmartWindowUnit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("principal",id);
        List<SmartWindowUnit> unit = smartWindowUnitService.list(queryWrapper);

        for(SmartWindowUnit U : unit){
            if(U.getId() == null){
                break;
            }
            else {
                List<SysRole> role=smartWindowUnitService.getUser();
                for(SysRole r:role) {
                    try {
                        MessageDTO messageDTO = new MessageDTO();
                        messageDTO.setTitle("???????????????????????????????????????");
                        messageDTO.setContent("????????????????????????:"+U.getPName() + "???,??????????????????" + U.getPrincipalName() +"???????????????????????????????????????????????????????????????");
                        messageDTO.setFromUser("admin");
                        messageDTO.setToUser(r.getUsername());
                        messageDTO.setCategory("1");

                        sysBaseAPI.sendSysAnnouncement(messageDTO);

                    } catch (NullPointerException e) {
                    }
                }
            }
        }
    }
    void SentInfCauseP(String id){
        QueryWrapper<SmartWindowPeople> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("person_id",id);
        List<SmartWindowPeople> person = smartWindowPeopleService.list(queryWrapper);
        for(SmartWindowPeople P : person){
            if(P.getId() == null){
                break;
            }
            else {
                List<SysRole> role2 = smartWindowPeopleService.getUser();
                for(SysRole r2 : role2) {
                    String departmentId = P.getDepartmentId();
                    String windowName = smartWindowPeopleService.getDepartmentNameByDepartmentId(departmentId);
                    try {
                        MessageDTO messageDTO = new MessageDTO();
                        messageDTO.setTitle("??????????????????????????????");
                        messageDTO.setContent("????????????????????????:"+windowName + "??????" + P.getPersonName() +"?????????????????????????????????");
                        messageDTO.setFromUser("admin");
                        messageDTO.setToUser(r2.getUsername());
                        messageDTO.setCategory("1");

                        sysBaseAPI.sendSysAnnouncement(messageDTO);

                    } catch (NullPointerException e) {
                    }
                }
            }
        }
    }

}
