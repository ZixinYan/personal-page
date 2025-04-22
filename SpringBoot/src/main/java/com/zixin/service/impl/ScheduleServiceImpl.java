package com.zixin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zixin.mapper.ScheduleMapper;
import com.zixin.pojo.PageBean;
import com.zixin.pojo.Project;
import com.zixin.pojo.Schedule;
import com.zixin.service.ScheduleService;
import com.zixin.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleMapper scheduleMapper;

    @Override
    public void add(Schedule schedule) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        schedule.setCreateUser(id);
        scheduleMapper.add(schedule);
    }

    @Override
    public PageBean<Schedule> list(Integer pageNum, Integer pageSize, String deadline, String finish, Integer priority){
        PageBean<Schedule> pageBean = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        List<Schedule> list = scheduleMapper.list(id, deadline,finish, priority);
        // 强转, 由于PageBean继承自ArrayList, 所以可以强转, 可以获取PageHelper的分页信息
        Page<Schedule> p = (Page<Schedule>) list;
        //填充到PageBean
        pageBean.setTotal(p.getTotal());
        pageBean.setItems(p.getResult());

        return pageBean;
    }

    @Override
    public void update(Schedule schedule) {
        scheduleMapper.update(schedule);
    }

    @Override
    public void delete(Integer id) {
        scheduleMapper.delete(id);
    }

}
