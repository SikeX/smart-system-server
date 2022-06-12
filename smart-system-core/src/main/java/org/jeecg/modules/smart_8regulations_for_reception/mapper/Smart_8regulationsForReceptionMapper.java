package org.jeecg.modules.smart_8regulations_for_reception.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.smart_8regulations_for_reception.entity.Smart_8regulationsForReception;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 八项规定公务接待
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
public interface Smart_8regulationsForReceptionMapper extends BaseMapper<Smart_8regulationsForReception> {
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
}
