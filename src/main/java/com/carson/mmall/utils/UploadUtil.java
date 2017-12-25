package com.carson.mmall.utils;

import com.carson.mmall.config.CustomConfig;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.exception.MmallException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class UploadUtil {
    @Autowired
    private CustomConfig customConfig;
    public static String uploadFile(MultipartFile file){
        if (file==null ||file.isEmpty()) {
            throw new MmallException(ResultEnum.PRODUCT_IMAGES_NOT_NULL);
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 文件上传后的路径
        String filePath = "";
        // 解决中文问题，liunx下中文路径，图片显示问题
        fileName = UUID.randomUUID() + suffixName;
        log.info("fileName={}",filePath+fileName);
        File dest = new File(filePath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            return fileName;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
