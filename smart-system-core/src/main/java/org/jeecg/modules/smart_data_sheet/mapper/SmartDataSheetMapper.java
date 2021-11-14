package org.jeecg.modules.smart_data_sheet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.smart_data_sheet.entity.SmartDataSheet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 资料库
 * @Author: jeecg-boot
 * @Date:   2021-11-11
 * @Version: V1.0
 */
public interface SmartDataSheetMapper extends BaseMapper<SmartDataSheet> {
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
