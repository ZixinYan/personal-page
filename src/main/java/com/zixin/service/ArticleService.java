package com.zixin.service;

import com.zixin.pojo.Article;
import com.zixin.pojo.PageBean;

public interface ArticleService {

    void add(Article article);

    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);

    void update(Article article);

    void delete(Integer id);
}
