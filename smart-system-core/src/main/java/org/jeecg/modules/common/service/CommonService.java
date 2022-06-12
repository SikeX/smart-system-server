package org.jeecg.modules.common.service;

import java.util.List;
import java.util.Map;

public interface CommonService {
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
     * 根据部门编码获所有子部门的ID
     *
     * @param orgCode 部门编码
     * @return 子部门ID列表
     */
    String getChildrenIdStringByOrgCode(String orgCode);
    /**
     * 根据部门ID查询部门名称
     *
     * @param departIds 部门ID
     * @return 部门名称
     */
    Map<String, String> getDepNamesByIds(List<String> departIds);
}
