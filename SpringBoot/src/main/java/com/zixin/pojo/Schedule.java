package com.zixin.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Schedule {

    private Integer id;
    @NotEmpty
    private String content;
    @NotEmpty
    private String finish;
    private Integer createUser;
    @NotNull
    private Integer priority;
    @NotNull
    private Integer predictTime;
    @NotNull
    private Integer actualTime;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deadline;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
