package org.jeecg.modules.system.vo;

import lombok.Data;

import java.util.List;

@Data
/**
 * @Description: TODO
 * @author: scott
 * @date: 2021年11月18日 12:31
 */
public class UserInfo {
    private String departCode;
    private String names;
    private List<String> leaders;
}
