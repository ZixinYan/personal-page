package com.zixin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zixin.mapper.ProjectMapper;
import com.zixin.pojo.Article;
import com.zixin.pojo.PageBean;
import com.zixin.pojo.Project;
import com.zixin.pojo.Result;
import com.zixin.service.ProjectService;
import com.zixin.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    ProjectMapper projectMapper;

    @Override
    public void add(Project project) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        project.setCreateUser(id);
        projectMapper.add(project);
    }

    @Override
    public PageBean<Project> list(Integer pageNum, Integer pageSize, Integer categoryId, String status) {
        PageBean<Project> pageBean = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        List<Project> list = projectMapper.list(id,categoryId, status);
        // 强转, 由于PageBean继承自ArrayList, 所以可以强转, 可以获取PageHelper的分页信息
        Page<Project> p = (Page<Project>) list;
        //填充到PageBean
        pageBean.setTotal(p.getTotal());
        pageBean.setItems(p.getResult());

        return pageBean;
    }

    @Override
    public void update(Project project) {
        projectMapper.update(project);
    }

    @Override
    public void delete(Integer id) {
        projectMapper.delete(id);
    }
}
