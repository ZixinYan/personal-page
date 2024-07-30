package com.zixin.mapper;

import com.zixin.pojo.Article;
import com.zixin.pojo.Project;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProjectMapper {
    @Insert("insert into project(projectName, categoryId, projectUrl, briefNote, createUser, create_time, update_time,status) " +
            "values(#{projectName}, #{categoryId}, #{projectUrl}, #{briefNote}, #{createUser}, now(), now(),#{status})")
    void add(Project project);

    List<Project> list(Integer id, Integer categoryId, String status);

    @Update("update project set projectName=#{projectName}, categoryId=#{categoryId}, " +
            "projectUrl=#{projectUrl}, briefNote=#{briefNote}, " +
            "update_time=now() where id=#{id}")
    void update(Project project);

    @Delete("delete from project where id=#{id}")
    void delete(Integer id);
}
