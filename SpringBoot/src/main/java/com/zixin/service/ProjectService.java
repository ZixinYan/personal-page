package com.zixin.service;

import com.zixin.pojo.PageBean;
import com.zixin.pojo.Project;
import org.springframework.stereotype.Service;

@Service
public interface ProjectService {
    void add(Project project);

    PageBean<Project> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);

    void update(Project project);

    void delete(Integer id);
}
