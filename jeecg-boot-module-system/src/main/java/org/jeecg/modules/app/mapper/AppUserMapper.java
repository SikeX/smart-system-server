package org.jeecg.modules.app.mapper;

import org.apache.ibatis.annotations.*;
import org.jeecg.modules.app.entity.AppUser;


@Mapper
public interface AppUserMapper {
    @Insert("insert into tb_app_user"
            + "(client_id, android_id, last_login_ip, mac, app_version, brand, phone_model, last_login_time, status, ctime, mtime)    "
            + "values"
            + "(#{clientId}, #{androidId}, #{lastLoginIp}, #{mac}, #{appVersion}, #{brand}, #{phoneModel}, #{lastLoginTime}, #{status}, #{ctime}, #{mtime}) ")
    int insert(AppUser appUser);

    @Select("SELECT * FROM tb_app_user WHERE id = #{id}")
    AppUser queryById(int id);

    @Select("SELECT * FROM tb_app_user WHERE android_id = #{androidId}")
    AppUser queryByAndroidId(String androidId);

    @Update("UPDATE tb_app_user SET last_login_ip = #{lastLoginIp}, mac = #{mac}, app_version = #{appVersion}, " +
            "brand = #{brand}, phone_model = #{phoneModel}, last_login_time = #{lastLoginTime}, mtime = #{mtime}, " +
            "sys_user_id = #{sysUserId} where id = #{id}")
    int updateById(AppUser appUser);

    @Update("UPDATE tb_app_user SET sys_user_id = #{sysUserId}, mtime = #{mtime} " +
            "where id = #{id}")
    int updateSysUserIdById(@Param("id") int id, @Param("sysUserId") String sysUserId, @Param("mtime") int mtime);
}
