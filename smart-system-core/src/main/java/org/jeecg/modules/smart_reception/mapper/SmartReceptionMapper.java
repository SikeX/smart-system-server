package org.jeecg.modules.smart_reception.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.smart_reception.entity.SmartReception;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 公务接待2.0
 * @Author: jeecg-boot
 * @Date:   2022-02-28
 * @Version: V1.0
 */
public interface SmartReceptionMapper extends BaseMapper<SmartReception> {
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
