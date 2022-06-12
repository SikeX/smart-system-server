package org.jeecg.modules.faceRecognition.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: sike
 * @Date: 2022/3/28 15:23
 * @Version: V1.0
 */
@Data
public class FaceVo implements Serializable {

    private String userId;

    private String userName;
}
