package com.zixin.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Data
public class Project {
    private Integer id;
    @NotEmpty
    @Pattern(regexp = "^[\\s\\S]{1,10}$",message = "标题长度必须在1-10之间")
    private String projectName;
    @NotNull
    private Integer categoryId;
    private Integer createUser;
    @NotEmpty
    @URL
    private String projectUrl;
    @NotEmpty
    private String briefNote;
    @NotEmpty
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;//更新时间
}
