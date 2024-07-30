package com.zixin.controller;

import com.zixin.pojo.Article;
import com.zixin.pojo.PageBean;
import com.zixin.pojo.Project;
import com.zixin.pojo.Result;
import com.zixin.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping("/add")
    public Result add(@RequestBody @Validated Project project){
        projectService.add(project);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<PageBean<Project>> list(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String status
    ){
        PageBean<Project> pageBean = projectService.list(pageNum, pageSize, categoryId, status);
        return Result.success(pageBean);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated Project project){
        projectService.update(project);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result delete(Integer id){
        projectService.delete(id);
        return Result.success();
    }
}
