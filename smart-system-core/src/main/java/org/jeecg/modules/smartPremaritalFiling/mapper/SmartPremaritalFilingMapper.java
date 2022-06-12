package org.jeecg.modules.smartPremaritalFiling.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.smartPremaritalFiling.entity.SmartPremaritalFiling;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 8项规定婚前报备表
 * @Author: jeecg-boot
 * @Date:   2021-11-13
 * @Version: V1.0
 */
public interface SmartPremaritalFilingMapper extends BaseMapper<SmartPremaritalFiling> {
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
}
