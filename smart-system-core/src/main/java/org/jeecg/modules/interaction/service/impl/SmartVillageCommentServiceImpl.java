package org.jeecg.modules.interaction.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.interaction.domain.SmartVillageComment;
import org.jeecg.modules.interaction.service.SmartVillageCommentService;
import org.jeecg.modules.interaction.mapper.SmartVillageCommentMapper;
import org.jeecg.modules.interaction.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author sike
* @description 针对表【smart_village_comment】的数据库操作Service实现
* @createDate 2021-11-26 11:53:45
*/
@Service
public class SmartVillageCommentServiceImpl extends ServiceImpl<SmartVillageCommentMapper, SmartVillageComment>
    implements SmartVillageCommentService{

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Resource
    private SmartVillageCommentMapper smartVillageCommentMapper;

    @Override
    public IPage<CommentVo> getCommentListPage(Page<CommentVo> page, String userId){
        String departId = sysBaseAPI.getDepartIdByUserId(userId);

        return page.setRecords(smartVillageCommentMapper.getCommentList(page, departId));
    }

}




