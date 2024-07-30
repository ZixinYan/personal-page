package com.zixin.service;

import com.zixin.pojo.Category;

import java.util.List;

public interface CategoryService {
    void addArticle(Category category);

    List<Category> list();

    Category detail(Integer id);

    void update(Category category);

    void delete(Integer id);

    void addProject(Category category);
}
