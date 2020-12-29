package com.baizhi.serviceImpl;

import com.baizhi.dao.AdminMapper;
import com.baizhi.dao.LogMapper;
import com.baizhi.entity.Admin;
import com.baizhi.entity.AdminExample;
import com.baizhi.entity.Log;
import com.baizhi.entity.UserExample;
import com.baizhi.service.AdminService;
import com.baizhi.service.LogService;
import com.baizhi.util.Md5Utils;
import com.baizhi.util.UUIDUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author ：薛岩松
 * @time ：2020/12/18-18:54
 */
@Service("logService")
@Transactional
public class LogServiceImpl implements LogService {
    @Resource
    LogMapper logMapper;


    @Override
    public HashMap<String, Object> queryLogPage(Integer page, Integer rows) {
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
        List<Log> logs= logMapper.selectByExampleAndRowBounds(example, rowBounds);

        map.put("rows",logs);

        //查询总条数
        int records = logMapper.selectCountByExample(example);
        map.put("records",records);

        //计算总页数
        Integer tolal=records%rows==0?records/rows:records/rows+1;
        map.put("total",tolal);

        return map;
    }

    @Override
    public void edit(Log log) {
        logMapper.updateByPrimaryKeySelective(log);
    }

    @Override
    public void del(Log log) {
        logMapper.deleteByPrimaryKey(log);
    }

}
