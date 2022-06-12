package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysDepart;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 * 部门 Mapper 接口
 * <p>
 * 
 * @Author: Steve
 * @Since：   2019-01-22
 */
public interface SysDepartMapper extends BaseMapper<SysDepart> {
	
	/**
	 * 根据用户ID查询部门集合
	 */
	public List<SysDepart> queryUserDeparts(@Param("userId") String userId);

	/**
	 * 根据用户名查询部门
	 *
	 * @param username
	 * @return
	 */
	public List<SysDepart> queryDepartsByUsername(@Param("username") String username);

	@Select("select id from sys_depart where org_code=#{orgCode}")
	public String queryDepartIdByOrgCode(@Param("orgCode") String orgCode);

	@Select("select id,parent_id from sys_depart where id=#{departId}")
	public SysDepart getParentDepartId(@Param("departId") String departId);

	/**
	 *  根据部门Id查询,当前和下级所有部门IDS
	 * @param departId
	 * @return
	 */
	List<String> getSubDepIdsByDepId(@Param("departId") String departId);

	/**
	 * 根据部门编码获取部门下所有IDS
	 * @param orgCodes
	 * @return
	 */
	List<String> getSubDepIdsByOrgCodes(@org.apache.ibatis.annotations.Param("orgCodes") String[] orgCodes);

    List<SysDepart> queryTreeListByPid(@Param("parentId") String parentId);
	/**
	 * 根据id下级部门数量
	 * @param parentId
	 * @return
	 */
	@Select("SELECT count(*) FROM sys_depart where del_flag ='0' AND parent_id = #{parentId,jdbcType=VARCHAR}")
    Integer queryCountByPid(@Param("parentId")String parentId);
	/**
	 * 根据OrgCod查询所属公司信息
	 * @param orgCode
	 * @return
	 */
	SysDepart queryCompByOrgCode(@Param("orgCode")String orgCode);
	/**
	 * 根据id下级部门
	 * @param parentId
	 * @return
	 */
	@Select("SELECT * FROM sys_depart where del_flag ='0' AND parent_id = #{parentId,jdbcType=VARCHAR}")
	List<SysDepart> queryDeptByPid(@Param("parentId")String parentId);
	/**
	 * 根据id下级业务部门
	 * @param deptId
	 * @return
	 */
	@Select("SELECT * FROM sys_depart where del_flag ='0' AND work_parent_id = #{deptId,jdbcType=VARCHAR}")
	List<SysDepart> queryWorkChildrenDeparts(@Param("deptId")String deptId);
	/**
	 * 根据部门id获取部门信息
	 * @param deptId
	 * @return
	 */
	@Select("SELECT * FROM sys_depart where del_flag ='0' AND id = #{deptId,jdbcType=VARCHAR}")
	SysDepart queryDeptByDepartId(@Param("deptId")String deptId);
	/**
	 * 根据用户id获取用户所在部门
	 * @param userId
	 * @return
	 */
	SysDepart queryCurrentUserDepart(@Param("userId")String userId);

	@Select("SELECT * FROM sys_depart d where del_flag = '0' and depart_name REGEXP '.*局'" )
    List<SysDepart> getAllBusDepart();

	@Select("SELECT * FROM sys_depart where del_flag = '0' and org_code REGEXP concat('^',#{orgCode}, '.{3}$') ")
	List<SysDepart> getChildrenDepart(@Param("orgCode") String orgCode);

	/**
	 * 根据OrgCod查询所属公司信息
	 * @param name
	 * @return
	 */
	String getZhenIdByName(@Param("name")String name);
}
