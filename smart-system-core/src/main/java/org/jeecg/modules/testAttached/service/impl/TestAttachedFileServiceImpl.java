package org.jeecg.modules.testAttached.service.impl;

import org.jeecg.modules.testAttached.entity.TestAttachedFile;
import org.jeecg.modules.testAttached.mapper.TestAttachedFileMapper;
import org.jeecg.modules.testAttached.service.ITestAttachedFileService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 附件测试附表
 * @Author: jeecg-boot
 * @Date:   2021-11-07
 * @Version: V1.0
 */
@Service
public class TestAttachedFileServiceImpl extends ServiceImpl<TestAttachedFileMapper, TestAttachedFile> implements ITestAttachedFileService {
	
	@Autowired
	private TestAttachedFileMapper testAttachedFileMapper;
	
	@Override
	public List<TestAttachedFile> selectByMainId(String mainId) {
		return testAttachedFileMapper.selectByMainId(mainId);
	}
}
