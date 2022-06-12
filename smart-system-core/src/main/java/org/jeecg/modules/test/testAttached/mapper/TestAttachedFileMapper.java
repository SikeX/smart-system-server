package org.jeecg.modules.test.testAttached.mapper;

import java.util.List;

import org.jeecg.modules.test.testAttached.entity.TestAttachedFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 附件测试附表
 * @Author: jeecg-boot
 * @Date:   2021-11-07
 * @Version: V1.0
 */
public interface TestAttachedFileMapper extends BaseMapper<TestAttachedFile> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<TestAttachedFile> selectByMainId(@Param("mainId") String mainId);
}
