package org.jeecg.modules.app.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
    void updateSysUserIdById(int id, String sysUserId, int mtime);

    @Select("SELECT * FROM tb_wx_user WHERE wx_session_key = #{sessionKey}")
    WXUser queryBySessionKey(String sessionKey);

    @Update("UPDATE tb_wx_user SET phone = #{purePhoneNumber} where id = #{id}")
    void updatePhoneById(int id, String purePhoneNumber);

    @Update("UPDATE tb_wx_user SET sys_user_id = #{sysUserId} where id = #{id}")
    void updateWxUserSysUserIdById(int id, String sysUserId);
}
