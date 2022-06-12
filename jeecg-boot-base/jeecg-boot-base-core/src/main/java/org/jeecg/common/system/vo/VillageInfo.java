package org.jeecg.common.system.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: sike
 * @Date: 2022/3/31 22:16
 * @Version: V1.0
 */
@Data
public class VillageInfo implements Serializable {
    private String locationName;

    private Integer homeNumber;

    private Integer population;
}
