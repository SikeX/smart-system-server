package org.jeecg.modules.testAttached.service;

import org.jeecg.modules.testAttached.entity.TestAttachedFile;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 附件测试附表
 * @Author: jeecg-boot
 * @Date:   2021-11-07
 * @Version: V1.0
 */
public interface ITestAttachedFileService extends IService<TestAttachedFile> {

	public List<TestAttachedFile> selectByMainId(String mainId);
}
