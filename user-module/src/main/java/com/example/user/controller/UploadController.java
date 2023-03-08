package com.example.user.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.example.user.dto.FileDTO;
import com.example.user.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Value("${upload.path}")
    private String uploadPath;



    @PostMapping("/file")
    public Result uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            log.info("文件类型为：" + file.getContentType());
            String type= file.getContentType();
            // 获取原始文件名称
            String originalFilename = file.getOriginalFilename();
            // 生成新文件名
            FileDTO fileDTO = createNewFileName(originalFilename, type);
            // 保存文件
            file.transferTo(new File(uploadPath, fileDTO.getFileName()));
            // 返回结果
            log.debug("文件上传成功，{}", fileDTO);
            return Result.ok(fileDTO);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    @GetMapping("/file/delete")
    public Result deleteBlogImg(@RequestParam("name") String filename) {
        File file = new File(uploadPath, filename);
        if (file.isDirectory()) {
            return Result.fail("错误的文件名称");
        }
        FileUtil.del(file);
        return Result.ok();
    }

    private FileDTO createNewFileName(String originalFilename, String type) {
        // 获取后缀
        String suffix = StrUtil.subAfter(originalFilename, ".", true);
        // 生成目录
        String name = UUID.randomUUID().toString();
        int hash = name.hashCode();
        int d1 = hash & 0xF;
        int d2 = (hash >> 4) & 0xF;
        // 判断目录是否存在
        FileDTO fileDTO = new FileDTO();
        if (type.substring(0, type.indexOf('/')).equals("video")) {
            File dir = new File(uploadPath, StrUtil.format("/videos/{}/{}", d1, d2));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 生成文件名
            String fileName = StrUtil.format("/videos/{}/{}/{}.{}", d1, d2, name, suffix);
            fileDTO.setFileName(fileName);
            fileDTO.setType(2);
            return fileDTO;
        }
        File dir = new File(uploadPath, StrUtil.format("/imgs/{}/{}", d1, d2));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 生成文件名
        String fileName =  StrUtil.format("/imgs/{}/{}/{}.{}", d1, d2, name, suffix);
        fileDTO.setFileName(fileName);
        fileDTO.setType(1);
        return fileDTO;
    }
}
