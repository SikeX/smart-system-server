package org.jeecg.modules.wePower.publicity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: sike
 * @Date: 2021/12/24 17:19
 * @Version: V1.0
 */
@Data
public class PublicityQuery implements Serializable {

    private List<PublicityCommon> location;

    private List<PublicityCommon> department;

    private List<PublicityCommon> type;

    private List<PublicityCommon> year;
}
