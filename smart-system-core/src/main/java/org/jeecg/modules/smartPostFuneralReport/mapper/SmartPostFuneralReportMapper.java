package org.jeecg.modules.smartPostFuneralReport.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.interaction.vo.CommentVo;
import org.jeecg.modules.smartPostFuneralReport.entity.SmartPostFuneralReport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.smartPostFuneralReport.vo.FuneralReport;
import org.jeecg.modules.smartPostMarriage.entity.SmartPostMarriageReport;

/**
 * @Description: 丧事事后报备表
 * @Author: jeecg-boot
 * @Date:   2022-02-26
 * @Version: V1.0
 */
public interface SmartPostFuneralReportMapper extends BaseMapper<SmartPostFuneralReport> {
    public FuneralReport getFuneralReport(@Param("id") String id);
    public String getDepByOrgCode(@Param("orgCode") String orgCode);
    public String getDicText(@Param("dictId") String dictId,@Param("itemValue") String itemValue);
    public String getPostByCode(@Param("code") String code);
    SmartPostFuneralReport getByPreId(@Param("preId")String preId);
}
