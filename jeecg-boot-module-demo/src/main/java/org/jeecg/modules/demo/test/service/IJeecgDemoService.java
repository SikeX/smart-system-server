package org.jeecg.modules.demo.test.service;

import org.jeecg.common.system.base.service.JeecgService;
import org.jeecg.modules.demo.test.entity.JeecgDemo;

import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Description: jeecg 测试demo
 * @Author: jeecg-boot
 * @Date:  2018-12-29
 * @Version: V1.0
 */
public interface IJeecgDemoService extends JeecgService<JeecgDemo> {
	
	public void testTran();
	
	public JeecgDemo getByIdCacheable(String id);
	
	/**
	 * 查询列表数据 在service中获取数据权限sql信息
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	IPage<JeecgDemo> queryListWithPermission(int pageSize,int pageNo);

	/**
	 * 根据用户权限获取导出字段
	 * @return
	 */
	String getExportFields();

//	/**
//	 * 获取榆树镇互动数
//	 * @return
//	 */
//	String getYushuZ();

//	/**
//	 * 获取新农镇互动数
//	 * @return
//	 */
//	String getXinnongZ();
//
//	/**
//	 * 获取新发镇互动数
//	 * @return
//	 */
//	String getXinfaZ();
//
//	/**
//	 * 获取太平镇互动数
//	 * @return
//	 */
//	String getTaipingZ();
}
