package org.jeecg.modules.test.testAttached.service;

import org.jeecg.modules.test.testAttached.entity.TestAttached;
import org.jeecg.modules.test.testAttached.entity.TestAttachedFile;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 附件测试主表
 * @Author: jeecg-boot
 * @Date:   2021-11-07
 * @Version: V1.0
 */
public interface ITestAttachedService extends IService<TestAttached> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(TestAttached testAttached,List<TestAttachedFile> testAttachedFileList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(TestAttached testAttached,List<TestAttachedFile> testAttachedFileList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
