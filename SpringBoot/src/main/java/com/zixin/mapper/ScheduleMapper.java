package com.zixin.mapper;

import com.zixin.pojo.Schedule;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

@Mapper
public interface ScheduleMapper {

    @Insert("insert into schedule (content, finish, create_user,priority,predict_time,actual_time,create_time,update_time,deadline) " +
            "values (#{content}, #{finish} ,#{createUser},#{priority},#{predictTime},#{actualTime}, now(),now(),#{deadline})")
    void add(Schedule schedule);

    List<Schedule> list(Integer id, String deadline, String finish, Integer priority);

    @Update("update schedule set content=#{content}, finish=#{finish}, priority=#{priority}, " +
            "predict_time=#{predictTime}, actual_time=#{actualTime}, deadline=#{deadline}, update_time=now() where id=#{id}")
    void update(Schedule schedule);

    @Delete("delete from schedule where id=#{id}")
    void delete(Integer id);
}
