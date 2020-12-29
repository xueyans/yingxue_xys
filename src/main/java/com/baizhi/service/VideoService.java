package com.baizhi.service;

import com.baizhi.entity.Video;
import com.baizhi.po.VideoVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

public interface VideoService {

    HashMap<String,Object> queryVideoPage(Integer page, Integer rows);

    String add(Video video);

    void uploadVideoCoverAliyun(MultipartFile headImg, String id, HttpServletRequest request);

    String edit(Video video);

    void del(Video video);

    void delVideo(Video video);

    Video status(Video video);

    List<VideoVO> queryByReleaseTime();

}
