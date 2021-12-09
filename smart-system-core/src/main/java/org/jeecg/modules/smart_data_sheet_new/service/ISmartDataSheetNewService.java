package org.jeecg.modules.smart_data_sheet_new.service;

import org.jeecg.modules.smart_data_sheet_new.entity.SmartDataSheetNew;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 资料库
 * @Author: jeecg-boot
 * @Date:   2021-12-07
 * @Version: V1.0
 */
public interface ISmartDataSheetNewService extends IService<SmartDataSheetNew> {
    /**
     * 根据部门编码查询部门ID
     *
     * @param orgCode 部门编码
     * @return 部门ID
     */
    String getDepartIdByOrgCode(String orgCode);
    /**
     * 根据人员Id查询人员Name
     *
     * @param id 人员id
     * @return 部门Name
     */
    String getUserNameById(String id);
    /**
     * 根据部门编码获所有子部门的ID
     *
     * @param orgCode 部门编码
     * @return 子部门ID列表
     */
    List<String> getChildrenIdByOrgCode(String orgCode);

    String getDepartNameById(String id);
}
