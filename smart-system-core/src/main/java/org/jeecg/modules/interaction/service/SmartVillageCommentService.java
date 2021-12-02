package org.jeecg.modules.interaction.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.interaction.domain.SmartVillageComment;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.interaction.vo.CommentVo;

/**
* @author sike
* @description 针对表【smart_village_comment】的数据库操作Service
* @createDate 2021-11-26 11:53:45
*/
public interface SmartVillageCommentService extends IService<SmartVillageComment> {

    IPage<CommentVo> getCommentListPage(Page<CommentVo> page, String userId);

}
