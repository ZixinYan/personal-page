package com.zixin.controller;

import com.zixin.pojo.Category;
import com.zixin.pojo.Result;
import com.zixin.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/addArticle")
    public Result add(@RequestBody @Validated(Category.Add.class) Category category){
        categoryService.addArticle(category);
        return Result.success();
    }

    @PostMapping("/addProject")
    public Result addProject(@RequestBody @Validated(Category.Add.class) Category category){
        categoryService.addProject(category);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<Category>> list(){
        List<Category> list = categoryService.list();
        return Result.success(list);
    }

    @GetMapping("/detail")
    public Result<Category> detail(Integer id){
        Category category = categoryService.detail(id);
        return Result.success(category);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated(Category.Update.class) Category category){
        categoryService.update(category);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result delete(Integer id){
        categoryService.delete(id);
        return Result.success();
    }

}
