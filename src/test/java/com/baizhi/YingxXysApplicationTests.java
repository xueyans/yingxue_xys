package com.baizhi;

import com.baizhi.dao.AdminMapper;
import com.baizhi.entity.Admin;
import com.baizhi.entity.AdminExample;
import org.apache.ibatis.session.RowBounds;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class YingxXysApplicationTests {

    @Resource
    AdminMapper adminMapper;

    @Test
    void update() {
        Admin admin = new Admin();
        admin.setId("1");
        admin.setUsername("xiaohei");
        //adminMapper.updateByPrimaryKey(admin);
        //adminMapper.updateByPrimaryKeySelective(admin);

        AdminExample example = new AdminExample();
        example.createCriteria().andIdEqualTo("1");

        //根据条件修改
        //adminMapper.updateByExample(admin,example);
        adminMapper.updateByExampleSelective(admin,example);

    }

    @Test
    void insert() {
       Admin admin = new Admin("2","张三","123123","1","ASFCGB");
        adminMapper.insert(admin);
    }

    @Test
    void query() {

        //条件对象
        AdminExample example = new AdminExample();
        //设置条件
        example.createCriteria().andStatusEqualTo("1");
        //根据条件查询
        //List<Admin> admins = adminMapper.selectByExample(example);


        RowBounds rowBounds = new RowBounds(0,2);
        //List<Admin> admins = adminMapper.selectByRowBounds(new Admin(), rowBounds);
        List<Admin> admins = adminMapper.selectByExampleAndRowBounds(example, rowBounds);

        admins.forEach(admin -> System.out.println(admin));
    }


}
