package org.jeecg.modules.villageHome.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.modules.smartJob.entity.SysUser;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @Description:
 * @Author: sike
 * @Date: 2021/12/1 0:06
 * @Version: V1.0
 */
@Data
public class HomeVo {
    private String id;

    private String departId;

    private String address;

    private String homeCode;

    private String homeSurname;

    private String hostId;

    private String realname;

    private String phone;

    private List<SysUser> userList;
}
