package com.zixin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zixin.mapper.ArticleMapper;
import com.zixin.pojo.Article;
import com.zixin.pojo.PageBean;
import com.zixin.service.ArticleService;
import com.zixin.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired  // 依赖注入
    ArticleMapper articleMapper;

    @Override
    public void add(Article article) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        article.setCreateUser(id);
        articleMapper.add(article);
    }

    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        PageBean<Article> pageBean = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        List<Article> list = articleMapper.list(id,categoryId, state);
        // 强转, 由于PageBean继承自ArrayList, 所以可以强转, 可以获取PageHelper的分页信息
        Page<Article> p = (Page<Article>) list;
        //填充到PageBean
        pageBean.setTotal(p.getTotal());
        pageBean.setItems(p.getResult());
        return pageBean;
    }

    @Override
    public void update(Article article) {
        articleMapper.update(article);
    }

    @Override
    public void delete(Integer id) {
        articleMapper.delete(id);
    }


}
