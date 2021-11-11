package org.jeecg.modules.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.common.vo.SysDepVo;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
public interface CommonMapper {
    /**
     * 根据部门编码查询部门ID
     *
     * @param orgCode 部门编码
     * @return 部门ID
     */
    String getDepartIdByOrgCode(String orgCode);
    /**
     * 根据部门编码获所有子部门的ID
     *
     * @param orgCode 部门编码
     * @return 子部门ID列表
     */
    List<String> getChildrenIdByOrgCode(String orgCode);

    /**
     * 根据部门ID查询部门名称
     *
     * @param departIds 部门ID列表
     * @return 部门名称
     */
    List<SysDepVo> getDepNamesByIds(@Param("departIds") List<String> departIds);
}
