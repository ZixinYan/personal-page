package com.zixin.mapper;

import com.zixin.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Select("select * from user where username =#{userName}")
    User findByUserName(String userName);
    @Insert("insert into user(username,password,create_time,update_time)" +
            " values(#{username},#{password},now(),now())")
    void add(String username, String password);

    @Update("update user set nickname=#{nickname},email=#{email},update_time=#{updateTime} where id = #{id}")
    void update(User user);
    @Update("update user set user_pic=#{avatarUrl},update_time=now() where id=#{id}")
    void updateAvatar(String avatarUrl,Integer id);

    @Update("update user set password=#{password},update_time=now() where id=#{id}")
    void updatePassword(String password,Integer id);

    @Select("select * from user where email =#{email}")
    User findByEmail(String email);

    @Update("update user set password=#{md5String},update_time=now() where email=#{email}")
    void updatePasswordByEmail(String md5String, String email);
}
