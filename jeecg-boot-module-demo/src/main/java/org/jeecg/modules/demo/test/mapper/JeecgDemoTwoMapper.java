package org.jeecg.modules.demo.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.demo.test.entity.JeecgDemo;
import org.jeecg.modules.demo.test.entity.SysDepart;
import org.jeecg.modules.demo.test.entity.partyUser;

import java.util.List;

/**
 * @Description: jeecg 测试demo
 * @Author: jeecg-boot
 * @Date:  2018-12-29
 * @Version: V1.0
 */
public interface JeecgDemoTwoMapper extends BaseMapper<SysDepart> {
    /**
     * 查询用户已授权字段
     * @return
     */
    public SysDepart getDaoli();
}
