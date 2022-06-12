package org.jeecg.modules.wePower.publicity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: sike
 * @Date: 2021/12/24 17:23
 * @Version: V1.0
 */
@Data
public class PublicityCommon {
    private String value;

    private String label;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<PublicityCommon> children;
}
