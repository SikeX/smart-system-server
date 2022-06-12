package org.jeecg.modules.wePower.info.vo;

import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: sike
 * @Date: 2022/3/31 17:21
 * @Version: V1.0
 */
@Data
public class WePowerInfoVo implements Serializable {

    @Dict(dicCode = "id",dicText = "depart_name",dictTable = "sys_depart")
    private String locationName;

    private Integer homeNumber;

    private Integer population;

    private List<People> dangZhiBuList;

    private List<People> committeeList;

    private List<People> dangYuanList;

    private List<People> villageRepresentativeList;

    private List<People> councilList;

    private List<People> shareholderList;

    private List<People> memberList;

    private List<People> representativeList;
}
