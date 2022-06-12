package org.jeecg.modules.wePower.info.vo;

import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

import java.io.Serializable;

/**
 * @Description:
 * @Author: sike
 * @Date: 2022/3/31 17:30
 * @Version: V1.0
 */
@Data
public class People implements Serializable {
    private String name;
    private String location;
    private String img;

//    @Dict(dicCode = "lead_job")
    private String dangZhiBuJob;
}
