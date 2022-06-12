package org.jeecg.modules.interaction.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.interaction.domain.SmartVillageComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.interaction.vo.CommentVo;

import java.util.List;

/**
* @author sike
* @description 针对表【smart_village_comment】的数据库操作Mapper
* @createDate 2021-11-26 11:53:45
* @Entity org.jeecg.modules.interaction.domain.SmartVillageComment
*/
public interface SmartVillageCommentMapper extends BaseMapper<SmartVillageComment> {

    public List<CommentVo> getCommentList(Page<CommentVo> page, @Param("departId") String departId);

}




