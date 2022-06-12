package org.jeecg.modules.SmartFirstFormPeople.entity;

import lombok.Data;

import java.util.List;

@Data
/**
 * @Description: TODO
 * @author: scott
 * @date: 2021年11月18日 12:31
 */
public class FirstFormInfo {
    private String departCode;
    private String interviewees;
    private List<String> leaders;
}
