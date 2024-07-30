package com.zixin.mapper;

import com.zixin.pojo.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @Insert("insert into category(category_name,category_alias,create_user,create_time,update_time,content) " +
            "values(#{categoryName},#{categoryAlias},#{createUser},now(),now(),'日记')")
    void addArticle(Category category);

    @Select("select * from category where create_user = #{id}")
    List<Category> list(Integer id);

    @Select("select * from category where id = #{id}")
    Category detail(Integer id);
    @Update("update category set category_name=#{categoryName},category_alias=#{categoryAlias},update_time=now() where id=#{id}")
    void update(Category category);
    @Delete("delete from category where id=#{id}")
    void delete(Integer id);

    @Insert("insert into category(category_name,category_alias," +
            "create_user,create_time,update_time,content) " +
            "values(#{categoryName},#{categoryAlias},#{createUser},now(),now(),'项目')")
    void addProject(Category category);
}
