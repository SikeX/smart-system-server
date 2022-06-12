package org.jeecg.modules.system.service;


import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserDepart;
import org.jeecg.modules.system.model.DepartIdModel;


import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * SysUserDpeart用户组织机构service
 * </p>
 * @Author ZhiLin
 *
 */
public interface ISysUserDepartService extends IService<SysUserDepart> {
	

	/**
	 * 根据指定用户id查询部门信息
	 * @param userId
	 * @return
	 */
	List<DepartIdModel> queryDepartIdsOfUser(String userId);
	

	/**
	 * 根据部门id查询用户信息
	 * @param depId
	 * @return
	 */
	List<SysUser> queryUserByDepId(String depId);
  	/**
	 * 根据部门code，查询当前部门和下级部门的用户信息
	 */
	List<SysUser> queryUserByDepCode(String depCode,String realname);

	/**
	 * 用户组件数据查询
	 * @param departId
	 * @param username
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	IPage<SysUser> queryDepartUserPageList(String departId, String username, String realname, int pageSize, int pageNo);
	//只展示本单位及下级单位人员
	IPage<SysUser> newqueryDepartUserPageList(String departId, String username, String realname, int pageSize, int pageNo);
	//只展示村民
	IPage<SysUser> queryDepartVillagePageList(String departId, String username, String realname, int pageSize, int pageNo);

	/**
	 * 政治生态文明系统使用，真正只查询本单位的用户，不查寻子单位
	 *
	 * @param departId 单位ID，必须
	 * @param username
	 * @param realname
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	IPage<SysUser> queryRealDepartUserPageList(String departId, String username, String realname, int pageSize, int pageNo);
}
