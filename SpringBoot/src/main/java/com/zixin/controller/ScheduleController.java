package com.zixin.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zixin.pojo.PageBean;
import com.zixin.pojo.Result;
import com.zixin.pojo.Schedule;
import com.zixin.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    // TODO
    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/add")
    public Result add(@RequestBody @Validated Schedule schedule){
        scheduleService.add(schedule);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<PageBean<Schedule>> list(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date deadline,
            @RequestParam(required = false) String finish,
            @RequestParam(required = false) Integer priority
    ){
        String deadlineStr = null;
        if (deadline != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            deadlineStr = sdf.format(deadline);
        }
        PageBean<Schedule> pageBean = scheduleService.list(pageNum, pageSize, deadlineStr, finish, priority );
        return Result.success(pageBean);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated Schedule schedule){
        scheduleService.update(schedule);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result delete(Integer id){
        scheduleService.delete(id);
        return Result.success();
    }


}
