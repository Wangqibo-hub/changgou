package com.changgou.controller;

import com.changgou.file.FastDFSFile;
import com.changgou.util.FastDFSClient;
import entity.Result;
import entity.StatusCode;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-10 15:05
 * @description
 */
@RestController
public class FileController {

    /**
    * @Description: 文件上传
    * @Param: [file]
    * @Return: java.lang.String
    * @Author: Wangqibo
    * @Date: 2020/8/10/0010
    */
    @PostMapping("upload")
    public Result<String> upload(@RequestParam("file")MultipartFile file) throws Exception {
        //封装一个FastDFSFile
        FastDFSFile fastDFSFile = new FastDFSFile(
                file.getOriginalFilename(),//文件名
                file.getBytes(),//文件的字节数组
                StringUtils.getFilenameExtension(file.getOriginalFilename())//文件的扩展名
        );
        //上传文件
        String[] upload = FastDFSClient.upload(fastDFSFile);
        //组装文件上传地址
        String url = FastDFSClient.getTrackerUrl() + "/" + upload[0] + "/" + upload[1];
        return new Result<String>(true, StatusCode.OK,"文件上传成功",url);
    }
}
