package org.jeecg.modules.SmartPunishPeople.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

import java.util.List;

@Data
/**
 * @Description: TODO
 * @author: scott
 * @date: 2021年11月18日 12:31
 */
public class punishInfo {
    private String departCode;
    private String punishers;
    private List<String> leaders;
}
