package org.jeecg.modules.demo.test.entity;

import java.util.Date;

public class partyUser {
    String username;
    Date partyYear;

    public String getUserName()
    {
        return this.username;
    }

    public void setUserName(String username)
    {
        this.username = username;
    }

    public Date getPartyYear()
    {
        return this.partyYear;
    }

    public void setPartyYear(Date partyYear)
    {
        this.partyYear = partyYear;
    }
}
