package org.jeecg.modules.common.service.impl;

import com.google.common.base.Joiner;
import org.jeecg.modules.common.mapper.CommonMapper;
import org.jeecg.modules.common.service.CommonService;
import org.jeecg.modules.common.vo.SysDepVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    CommonMapper commonMapper;

    @Override
    public String getDepartIdByOrgCode(String orgCode) {
        return commonMapper.getDepartIdByOrgCode(orgCode);
    }

    @Override
    public List<String> getChildrenIdByOrgCode(String orgCode) {
        orgCode += "%";
        return commonMapper.getChildrenIdByOrgCode(orgCode);
    }

    /**
     * 根据部门编码获所有子部门的ID
     *
     * @param orgCode 部门编码
     * @return 子部门ID列表
     */
    @Override
    public String getChildrenIdStringByOrgCode(String orgCode) {
        orgCode += "%";
        List<String> childrenIdList = commonMapper.getChildrenIdByOrgCode(orgCode);
        return Joiner.on(",").join(childrenIdList);
    }

    /**
     * 根据部门ID查询部门名称
     *
     * @param departIds 部门ID
     * @return 部门名称
     */
    @Override
    public Map<String, String> getDepNamesByIds(List<String> departIds) {
        List<SysDepVo> list = commonMapper.getDepNamesByIds(departIds);
        Map<String, String> res = new HashMap<String, String>();
        list.forEach(item -> {
                    if (res.get(item.getId()) == null) {
                        res.put(item.getId(), item.getDepartName());
                    }
                }
        );
        return res;
    }
}
