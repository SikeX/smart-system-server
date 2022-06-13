package org.jeecg.modules.demo.test.service;

import org.jeecg.common.system.base.service.JeecgService;
import org.jeecg.modules.demo.test.entity.Cloud;
import org.jeecg.modules.demo.test.entity.JeecgDemo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.demo.test.entity.partyUser;
import org.jeecg.modules.demo.test.vo.SysDepartTreeModel;
import org.jeecg.modules.demo.test.vo.DepartIdModel;

import java.util.List;

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

	/**
	 * 获取榆树镇互动数
	 * @return
	 */
	String getYushuZ();

	/**
	 * 获取新农镇互动数
	 * @return
	 */
	String getXinnongZ();

	/**
	 * 获取新发镇互动数
	 * @return
	 */
	String getXinfaZ();

	/**
	 * 获取太平镇互动数
	 * @return
	 */
	String getTaipingZ();

	/**
	 * 获取通知数
	 * @return
	 */
	String getTongzhi();

	/**
	 * 获取廉政数
	 * @return
	 */
	String getLianzheng();

	/**
	 * 获取任务数
	 * @return
	 */
	String getRenwu();

	/**
	 * 获取审核数
	 * @return
	 */
	String getShenhe();

	/**
	 * 获取通知未读数
	 * @return
	 */
	String getTongzhiweidu();

	/**
	 * 获取通知已读数
	 * @return
	 */
	String getTongzhiyidu();

	/**
	 * 获取廉政未读数
	 * @return
	 */
	String getLianzhengweidu();

	/**
	 * 获取廉政已读数
	 * @return
	 */
	String getLianzhengyidu();

	/**
	 * 获取我的任务数
	 * @return
	 */
	String getRenwutiao();

	/**
	 * 获取我的任务数
	 * @return
	 */
	String getDaishenhe();

	/**
	 * 获取我的任务数
	 * @return
	 */
	String getYishenhe();

	/**
	 * 获取我的任务数
	 * @return
	 */
	List<partyUser> getCloudData();


}
