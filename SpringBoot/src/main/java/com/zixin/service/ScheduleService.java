package com.zixin.service;

import com.zixin.pojo.PageBean;
import com.zixin.pojo.Schedule;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface ScheduleService {
    void add(Schedule schedule);

    PageBean<Schedule> list(Integer pageNum, Integer pageSize, String deadline, String finish, Integer priority);

    void update(Schedule schedule);

    void delete(Integer id);

}
