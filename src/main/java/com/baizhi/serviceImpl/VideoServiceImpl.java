package com.baizhi.serviceImpl;

import com.baizhi.annotation.AddLog;
import com.baizhi.dao.VideoMapper;
import com.baizhi.entity.UserExample;
import com.baizhi.entity.Video;
import com.baizhi.entity.VideoExample;
import com.baizhi.po.VideoPO;
import com.baizhi.po.VideoVO;
import com.baizhi.service.VideoService;
import com.baizhi.util.AliyunOSSUtil;
import com.baizhi.util.UUIDUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author UserServiceImpl
 * @time 2020/12/21-11:37
 */
@Service
@Transactional
public class VideoServiceImpl implements VideoService {


    @Resource
    VideoMapper videoMapper;

    @Override
    public HashMap<String, Object> queryVideoPage(Integer page, Integer rows) {
        // Integer page, Integer rows(每页展示条数)
        //返回  page=当前页   rows=[User,User]数据    tolal=总页数   records=总条数
        HashMap<String, Object> map = new HashMap<>();

        //设置当前页
        map.put("page",page);
        //创建条件对象
        UserExample example = new UserExample();
        //创建分页对象   参数：从第几条开始，展示几条
        RowBounds rowBounds = new RowBounds((page-1)*rows,rows);
        //查询数据
        List<Video> videos = videoMapper.selectByExampleAndRowBounds(example, rowBounds);


        map.put("rows",videos);

        //查询总条数
        int records = videoMapper.selectCountByExample(example);
        map.put("records",records);

        //计算总页数
        Integer tolal=records%rows==0?records/rows:records/rows+1;
        map.put("total",tolal);

        return map;
    }

    @AddLog(value = "添加视频")
    @Override
    public String add(Video video) {
        String uuid = UUIDUtil.getUUID();
        video.setId(uuid);
        video.setUploadTime(new Date());
        video.setLikeCount("0");
        video.setStatus("1");
        video.setPlayCount("0");
        video.setGroupId("1");
        videoMapper.insertSelective(video);
        //添加方法返回id
        return uuid;
    }


    @Override
    public void uploadVideoCoverAliyun(MultipartFile videoPath, String id, HttpServletRequest request) {

        //获取文件名   动画.mp4
        String filename = videoPath.getOriginalFilename();
        //拼接时间戳  2341423424-动画.mp4
        String newName=new Date().getTime()+"-"+filename;
        //拼接视频名   video/2341423424-动画.mp4
        String objectName="video/"+newName;

        /*1.上传至阿里云
         * 将文件上传至阿里云
         * 参数：
         *   headImg：MultipartFile类型的文件
         *   bucketName:存储空间名
         *   objectName:文件名
         * */
        AliyunOSSUtil.uploadBytesFile(videoPath,"yingxue2006",objectName);

        //根据视频名拆分    0:2341423424-动画    1:mp4
        String[] split = newName.split("\\.");
        //获取视频名字  0:2341423424-动画   cover/2341423424-动画.jpg
        String coverName="cover/"+split[0]+".jpg";

        /*2.截取封面并上传
         * 视频截取帧并上传至阿里云
         * 参数：
         *   bucketName:存储空间名
         *   videoObjectName:视频文件名
         *   coverObjectName:封面文件名
         * */
        AliyunOSSUtil.videoInterceptCoverUpload("yingxue2006",objectName,coverName);

        //5.修改数据
        //修改的条件
        VideoExample example = new VideoExample();
        example.createCriteria().andIdEqualTo(id);

        Video video = new Video();
        video.setCoverPath("http://yingxue2006.oss-cn-beijing.aliyuncs.com/"+coverName); //设置封面
        video.setVideoPath("http://yingxue2006.oss-cn-beijing.aliyuncs.com/"+objectName); //设置视频地址

        //修改
        videoMapper.updateByExampleSelective(video, example);
    }

    @Override
    public void delVideo(Video video){
        int aa=video.getVideoPath().indexOf("video");
        int bb=video.getCoverPath().indexOf("cover");
        String c=video.getVideoPath().substring(aa);
        String d=video.getCoverPath().substring(bb);
        AliyunOSSUtil.deleteFile("yingxue2006",c);
        AliyunOSSUtil.deleteFile("yingxue2006",d);
    }

    @AddLog(value = "修改视频")
    @Override
    public String edit(Video video) {
        Video v=videoMapper.selectByPrimaryKey(video);
        if (video.getVideoPath().length()==0){
            video.setVideoPath(v.getVideoPath());
            video.setCoverPath(v.getCoverPath());
            videoMapper.updateByPrimaryKeySelective(video);
            return null;
        }else {
            delVideo(v);
            videoMapper.updateByPrimaryKeySelective(video);
        }
        return video.getId();
    }

    @AddLog(value = "删除视频")
    @Override
    public void del(Video video) {
        Video v=videoMapper.selectByPrimaryKey(video);
        delVideo(v);
        videoMapper.deleteByPrimaryKey(video);
    }

    @Override
    public Video status(Video video) {
        Video video1 = new Video();
        if (video.getStatus().equals("1")){
            video.setStatus("0");
            videoMapper.updateByPrimaryKeySelective(video);
            video1=videoMapper.selectByPrimaryKey(video);
            return video1;
        }
        if (video.getStatus().equals("0")){
            video.setStatus("1");
            videoMapper.updateByPrimaryKeySelective(video);
            video1=videoMapper.selectByPrimaryKey(video);
        }
        return video1;
    }

    @Override
    public List<VideoVO> queryByReleaseTime() {
        List<VideoPO> videoPOList = videoMapper.queryByReleaseTime();

        ArrayList<VideoVO> videoVOS = new ArrayList<>();

        for (VideoPO videoPO : videoPOList) {

            //封装VO对象
            VideoVO videoVO = new VideoVO(
                    videoPO.getId(),
                    videoPO.getTitle(),
                    videoPO.getCoverPath(),
                    videoPO.getVideoPath(),
                    videoPO.getUploadTime(),
                    videoPO.getDescription(),
                    18,  //根据videoPO中的视频id  redis  查询视频点赞数
                    videoPO.getCateName(),
                    videoPO.getHeadImg()
            );
            //将vo对象放入集合
            videoVOS.add(videoVO);
        }

        return videoVOS;
    }

}
