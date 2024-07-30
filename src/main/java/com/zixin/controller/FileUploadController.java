package com.zixin.controller;

import com.zixin.pojo.Result;
import com.zixin.utils.AliOSSUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {
        String name = file.getOriginalFilename();
        String filename = UUID.randomUUID().toString()+name.substring(name.lastIndexOf("."));
        String url = AliOSSUtils.uploadFile(filename,file.getInputStream());
        return Result.success(url);
    }
}
