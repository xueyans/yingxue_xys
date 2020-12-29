package com.baizhi.controller;

import com.baizhi.entity.Admin;
import com.baizhi.entity.Video;
import com.baizhi.service.VideoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author UserController
 * @time 2020/12/21-11:16
 */
@Controller
@RequestMapping("video")
public class VideoController {

    @Resource
    VideoService videoService;

    @ResponseBody
    @RequestMapping("queryVideoPage")
    public HashMap<String, Object> queryVideoPage(Integer page, Integer rows){
       
        return videoService.queryVideoPage(page, rows);
    }


    /*
    * 添加带图片的数据
    * 1.数据入库
    *    问题：
    *          文件上传问题
    *         图片地址有问题
    *  返回数据id
    * 2.文件上传
    * 3.修改图片数据
    * */
    @ResponseBody
    @RequestMapping("edit")
    public String edit(Video video, String oper){
        String id =null;
        if(oper.equals("add")){
             id = videoService.add(video);
        }
        if(oper.equals("edit")){
            id=videoService.edit(video);
        }
        if(oper.equals("del")){
            videoService.del(video);
        }
        return id;
    }

    @ResponseBody
    @RequestMapping("uploadVideoCover")
    public void uploadVideoCover(MultipartFile videoPath,String id, HttpServletRequest request){
        videoService.uploadVideoCoverAliyun(videoPath, id, request);
    }

    @ResponseBody
    @RequestMapping("status")
    public Video status(Video video){
        Video video1= videoService.status(video);
        return video1;
    }

}
