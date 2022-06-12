package org.jeecg.modules.smartPostMarriage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.smartJob.entity.SysUser;
import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 8项规定婚后报备表
 * @Author: jeecg-boot
 * @Date:   2021-11-10
 * @Version: V1.0
 */
public interface SmartPostMarriageReportMapper extends BaseMapper<SmartPostMarriageReport> {

    /**
     * 根据部门编码查询部门ID
     *
     * @param orgCode 部门编码
     * @return 部门ID
     */
    String getDepartIdByOrgCode(String orgCode);
    /**
     * 根据部门编码获得所有子部门的ID
     *
     * @param orgCode 部门编码
     * @return 子部门ID列表
     */
    List<String> getChildrenIdByOrgCode(String orgCode);

    SmartPostMarriageReport getByPreId(String preId);

    void editPreIsReport(String preId);

    void setPreIsReport(String preId);

    void setDelFlagByPreId(String preId);

    @Select("select * from sys_user where id=#{id}")
    SysUser getSysUser(String id);
}
