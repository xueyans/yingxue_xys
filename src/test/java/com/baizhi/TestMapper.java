package com.baizhi;

import com.baizhi.dao.AdminMapper;
import com.baizhi.entity.Admin;
import com.baizhi.entity.AdminExample;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author ：薛岩松
 * @time ：2020/12/18-19:29
 */
@SpringBootTest
public class TestMapper {
    @Resource
    AdminMapper adminMapper;

    @Test
    public void getName(){
        AdminExample example = new AdminExample();
        example.createCriteria().andUsernameEqualTo("xiaohei");//id为1

        Admin admin = adminMapper.selectOneByExample(example);
        System.out.println(admin);
    }
    @Test
    public void s(){
        String a="http://yingxue2006.oss-cn-beijing.aliyuncs.com/cover/1608725371886-动画.mp4";
        int aa=a.indexOf("cover");
        String c=a.substring(aa);
        System.out.println(c);
    }
}
