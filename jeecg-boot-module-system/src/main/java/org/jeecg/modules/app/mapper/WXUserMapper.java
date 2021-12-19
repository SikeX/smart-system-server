package org.jeecg.modules.app.mapper;

import org.apache.ibatis.annotations.*;
import org.jeecg.modules.app.entity.AppUser;
import org.jeecg.modules.app.entity.WXUser;


@Mapper
public interface WXUserMapper {
    @Insert("insert into tb_wx_user"
            + "(wx_open_id, wx_session_key, last_login_time, status, ctime, mtime)"
            + "values"
            + "(#{wxOpenId}, #{wxSessionKey}, #{lastLoginTime}, #{status}, #{ctime}, #{mtime})")
    int insert(WXUser wxUser);

    @Select("SELECT * FROM tb_wx_user WHERE id = #{id}")
    WXUser queryById(int id);

    @Select("SELECT * FROM tb_wx_user WHERE wx_open_id = #{openId}")
    WXUser queryByOpenId(String openId);

    @Update("UPDATE tb_wx_user SET last_login_time = #{lastLoginTime}, mtime = #{mtime}, " +
            "wx_session_key = #{wxSessionKey} where id = #{id}")
    int updateById(WXUser wxUser);

    @Update("UPDATE tb_wx_user SET sys_user_id = #{sysUserId}, mtime = #{mtime} " +
            "where id = #{id}")
    void updateSysUserIdById(@Param("id") int id, @Param("sysUserId") String sysUserId, @Param("mtime") int mtime);

    @Select("SELECT * FROM tb_wx_user WHERE wx_session_key = #{sessionKey}")
    WXUser queryBySessionKey(String sessionKey);

    @Update("UPDATE tb_wx_user SET phone = #{purePhoneNumber} where id = #{id}")
    void updatePhoneById(@Param("id") int id, @Param("purePhoneNumber") String purePhoneNumber);

    @Update("UPDATE tb_wx_user SET sys_user_id = #{sysUserId} where id = #{id}")
    void updateWxUserSysUserIdById(@Param("id") int id, @Param("sysUserId") String sysUserId);
}
