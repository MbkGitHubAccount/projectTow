package com.pinyougou.manager.controller;


import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import utils.FastDFSClient;

@RestController
@RequestMapping("/upload")
public class UploadController {

       @Value("${FILE_SERVER_URL}")
        private  String  FILE_SERVER_URL;


    @RequestMapping("/fileUpload")
    public Result fileUpload(MultipartFile file){
        //获取扩展名

        try {
            String filename = file.getOriginalFilename();

            String exName = filename.substring(filename.lastIndexOf(".")+1);

            // 2、 创建一个 FastDFS 的客户端

            FastDFSClient client=new FastDFSClient("classpath:/config/fdfs_client.conf");
            // 3、 执行上传处理
            String path = client.uploadFile(file.getBytes(), exName);

            // 4、 拼接返回的 url 和 ip 地址， 拼装成完整的 url
            String url = FILE_SERVER_URL + path;

            return new Result( true,url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传失败");
        }
    }




}
