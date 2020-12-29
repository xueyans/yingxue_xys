package com.baizhi.serviceImpl;

import com.baizhi.annotation.AddLog;
import com.baizhi.dao.CategoryMapper;
import com.baizhi.dao.UserMapper;
import com.baizhi.entity.Category;
import com.baizhi.entity.CategoryExample;
import com.baizhi.entity.User;
import com.baizhi.entity.UserExample;
import com.baizhi.service.CategoryService;
import com.baizhi.service.UserService;
import com.baizhi.util.UUIDUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author UserServiceImpl
 * @time 2020/12/21-11:37
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Resource
    CategoryMapper categoryMapper;

    @Override
    public HashMap<String, Object> queryCatePageOne(Integer page, Integer rows) {
        // Integer page, Integer rows(每页展示条数)
        //返回  page=当前页   rows=[User,User]数据    tolal=总页数   records=总条数
        HashMap<String, Object> map = new HashMap<>();

        //设置当前页
        map.put("page",page);
        //创建条件对象
        CategoryExample example = new CategoryExample();
        example.createCriteria().andLevelsEqualTo(1);
        //创建分页对象   参数：从第几条开始，展示几条
        RowBounds rowBounds = new RowBounds((page-1)*rows,rows);
        //查询数据
        List<Category> categories = categoryMapper.selectByExampleAndRowBounds(example, rowBounds);


        map.put("rows",categories);

        //查询总条数
        int records = categoryMapper.selectCountByExample(example);
        map.put("records",records);

        //计算总页数
        Integer tolal=records%rows==0?records/rows:records/rows+1;
        map.put("total",tolal);

        return map;
    }

    @Override
    public HashMap<String, Object> queryCatePageTwo(Integer page, Integer rows,String categoryId) {
        // Integer page, Integer rows(每页展示条数)
        //返回  page=当前页   rows=[User,User]数据    tolal=总页数   records=总条数
        HashMap<String, Object> map = new HashMap<>();

        //设置当前页
        map.put("page",page);
        //创建条件对象
        CategoryExample example = new CategoryExample();
        example.createCriteria().andLevelsEqualTo(2).andParentIdEqualTo(categoryId);
        //创建分页对象   参数：从第几条开始，展示几条
        RowBounds rowBounds = new RowBounds((page-1)*rows,rows);
        //查询数据
        List<Category> categories = categoryMapper.selectByExampleAndRowBounds(example, rowBounds);

        map.put("rows",categories);

        //查询总条数
        int records = categoryMapper.selectCountByExample(example);
        map.put("records",records);

        //计算总页数
        Integer tolal=records%rows==0?records/rows:records/rows+1;
        map.put("total",tolal);

        return map;
    }

    @AddLog(value = "添加类别")
    @Override
    public void add(Category category) {
        String uuid = UUIDUtil.getUUID();
        category.setId(uuid);
        if (category.getParentId()!=null){
            System.out.println("add"+1);
            category.setLevels(2);
        }else {
            System.out.println("add"+2);
            category.setLevels(1);
        }
        categoryMapper.insertSelective(category);
    }


    @AddLog(value = "修改类别")
    @Override
    public void edit(Category category) {
        System.out.println(category);
        categoryMapper.updateByPrimaryKeySelective(category);
    }

    @AddLog(value = "删除类别")
    @Override
    public String del(Category category) {
        System.out.println("del"+1);
        String message=null;
        if (category.getParentId()!=null) {
            categoryMapper.deleteByPrimaryKey(category);
            message="该二级类别删除成功";
            System.out.println("del"+2);
        }else {
            CategoryExample example = new CategoryExample();
            example.createCriteria().andParentIdEqualTo(category.getId());
            int count=categoryMapper.selectCountByExample(example);
            System.out.println(count);
            if (count==0){
                System.out.println("del"+3);
                categoryMapper.deleteByPrimaryKey(category);
                message="该一级类别删除成功";
            }else {
                System.out.println("del"+4);
                message="该类别下还有数据，不能删除";
            }
        }
        return message;
    }


}
