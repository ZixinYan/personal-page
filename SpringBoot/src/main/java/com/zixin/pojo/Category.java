package com.zixin.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class Category {
    @NotNull(message = "ID不能为空",groups = {Update.class})
    private Integer id;//主键ID
    @NotEmpty(message = "分类名称不能为空",groups = {Add.class,Update.class})
    private String categoryName;//分类名称
    @NotEmpty(message = "分类别名不能为空",groups = {Add.class,Update.class})
    private String categoryAlias;//分类别名
    private Integer createUser;//创建人ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;//更新时间
    @NotEmpty
    private String content;//分类描述
    public interface Add extends Default {};
    public interface Update extends Default{};
}
