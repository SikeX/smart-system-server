package org.jeecg.modules.demo.test.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_user")
public class partyUser implements Serializable {
    String realname;
    Date joinPartyDate;

//    public String getRealname()
//    {
//        return this.realname;
//    }
//
//    public void setRealname(String realname)
//    {
//        this.realname = realname;
//    }
//
//    public Date getPartyYear()
//    {
//        return this.partyYear;
//    }
//
//    public void setPartyYear(Date partyYear)
//    {
//        this.partyYear = partyYear;
//    }
}
