package org.jeecg.modules.test.testAttached.service.impl;

import org.jeecg.modules.test.testAttached.entity.TestAttached;
import org.jeecg.modules.test.testAttached.entity.TestAttachedFile;
import org.jeecg.modules.test.testAttached.mapper.TestAttachedFileMapper;
import org.jeecg.modules.test.testAttached.mapper.TestAttachedMapper;
import org.jeecg.modules.test.testAttached.service.ITestAttachedService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 附件测试主表
 * @Author: jeecg-boot
 * @Date:   2021-11-07
 * @Version: V1.0
 */
@Service
public class TestAttachedServiceImpl extends ServiceImpl<TestAttachedMapper, TestAttached> implements ITestAttachedService {

	@Autowired
	private TestAttachedMapper testAttachedMapper;
	@Autowired
	private TestAttachedFileMapper testAttachedFileMapper;
	
	@Override
	@Transactional
	public void saveMain(TestAttached testAttached, List<TestAttachedFile> testAttachedFileList) {
		testAttachedMapper.insert(testAttached);
		if(testAttachedFileList!=null && testAttachedFileList.size()>0) {
			for(TestAttachedFile entity:testAttachedFileList) {
				//外键设置
				entity.setMainId(testAttached.getId());
				testAttachedFileMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(TestAttached testAttached,List<TestAttachedFile> testAttachedFileList) {
		testAttachedMapper.updateById(testAttached);
		
		//1.先删除子表数据
		testAttachedFileMapper.deleteByMainId(testAttached.getId());
		
		//2.子表数据重新插入
		if(testAttachedFileList!=null && testAttachedFileList.size()>0) {
			for(TestAttachedFile entity:testAttachedFileList) {
				//外键设置
				entity.setMainId(testAttached.getId());
				testAttachedFileMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		testAttachedFileMapper.deleteByMainId(id);
		testAttachedMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			testAttachedFileMapper.deleteByMainId(id.toString());
			testAttachedMapper.deleteById(id);
		}
	}
	
}
