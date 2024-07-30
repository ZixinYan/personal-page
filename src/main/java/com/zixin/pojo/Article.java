package com.zixin.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zixin.anno.State;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import java.time.LocalDateTime;
@Data
public class Article {
    private Integer id;//主键ID
    @NotEmpty(message = "文章标题不能为空")
    @Pattern(regexp = "^[\\s\\S]{1,10}$",message = "文章标题长度必须在1-10之间")
    private String title;//文章标题
    @NotEmpty(message = "文章内容不能为空")
    private String content;//文章内容
    //@NotEmpty(message = "文章封面图像不能为空")
    @URL(message = "封面图像URL格式不正确")
    private String coverImg;//封面图像
    @State
    private String state;//发布状态 已发布|草稿
    @NotNull(message = "文章分类ID不能为空")
    private Integer categoryId;//文章分类id
    private Integer createUser;//创建人ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;//更新时间
}
