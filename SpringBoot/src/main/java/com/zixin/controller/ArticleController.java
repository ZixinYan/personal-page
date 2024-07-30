package com.zixin.controller;

import com.zixin.pojo.Article;
import com.zixin.pojo.PageBean;
import com.zixin.pojo.Result;
import com.zixin.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("/add")
    public Result add(@RequestBody @Validated Article article){
        articleService.add(article);
        return Result.success();
    }



    @GetMapping("/list")
    public Result<PageBean<Article>> list(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String state
    ){
       PageBean<Article> pageBean = articleService.list(pageNum, pageSize, categoryId, state);
        return Result.success(pageBean);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated Article article){
        articleService.update(article);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result delete(Integer id){
        articleService.delete(id);
        return Result.success();
    }
}
