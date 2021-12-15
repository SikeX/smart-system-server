package org.jeecg.modules.demo.test.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.system.base.service.JeecgService;
import org.jeecg.modules.demo.test.entity.JeecgDemo;
import org.jeecg.modules.demo.test.entity.SysDepart;
import org.jeecg.modules.demo.test.entity.partyUser;
import org.jeecg.modules.demo.test.vo.DepartIdModel;
import org.jeecg.modules.demo.test.vo.SysDepartTreeModel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: jeecg 测试demo
 * @Author: jeecg-boot
 * @Date:  2018-12-29
 * @Version: V1.0
 */
public interface IJeecgDemoServiceTwo extends IService<SysDepart> {
	


	/**
	 * 获取自然树各节点
	 * @return
	 */
	List<SysDepartTreeModel> getNaturalTree();

	/**
	 * 获取业务树各节点
	 * @return
	 */
	List<DepartIdModel> getWorkTree();
}
