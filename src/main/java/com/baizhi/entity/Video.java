package com.baizhi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "yx_video")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    @Id
    private String id;

    private String title;//标题

    private String description;//描述

    @Column(name = "video_path")
    private String videoPath;//视频链接

    @Column(name = "cover_path")
    private String coverPath;//封面链接

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//设置接受日期格式
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8" ) //响应日期格式
    @Column(name = "upload_time")
    private Date uploadTime;//上传时间

    @Column(name = "play_count")
    private String playCount;//播放次数

    @Column(name = "like_count")
    private String likeCount;//点赞次数

    @Column(name = "category_id")
    private String categoryId;//类别ID

    @Column(name = "group_id")
    private String groupId;//分组ID

    private String status;

    @Column(name = "user_id")
    private String userId;

}