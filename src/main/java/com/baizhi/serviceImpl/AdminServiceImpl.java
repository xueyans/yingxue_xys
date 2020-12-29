package com.baizhi.serviceImpl;

import com.baizhi.dao.AdminMapper;
import com.baizhi.entity.Admin;
import com.baizhi.entity.AdminExample;
import com.baizhi.entity.UserExample;
import com.baizhi.service.AdminService;
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
@Service("adminService")
@Transactional
public class AdminServiceImpl implements AdminService {
    @Resource
    AdminMapper adminMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Admin getAdmin(Admin admin) {
        Admin admin1=null;
        try{
            AdminExample example = new AdminExample();
            example.createCriteria().andUsernameEqualTo(admin.getUsername());//id为1
            admin1=adminMapper.selectOneByExample(example);
        }catch (Exception e){
            e.printStackTrace();
        }
        return admin1;
    }

    @Override
    public HashMap<String, Object> queryAdminPage(Integer page, Integer rows) {
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
        List<Admin> admins = adminMapper.selectByExampleAndRowBounds(example, rowBounds);

        map.put("rows",admins);

        //查询总条数
        int records = adminMapper.selectCountByExample(example);
        map.put("records",records);

        //计算总页数
        Integer tolal=records%rows==0?records/rows:records/rows+1;
        map.put("total",tolal);

        return map;
    }

    @Override
    public String add(Admin admin) {
        String uuid = UUIDUtil.getUUID();
        admin.setId(uuid);
        admin.setSalt(Md5Utils.getSalt(6));
        String password= Md5Utils.getMd5Code(admin.getSalt()+admin.getPassword());
        admin.setPassword(password);
        admin.setStatus("1");
        adminMapper.insertSelective(admin);

        //添加方法返回id
        return uuid;
    }

    @Override
    public void edit(Admin admin) {
        adminMapper.updateByPrimaryKeySelective(admin);
    }

    @Override
    public void del(Admin admin) {
        adminMapper.deleteByPrimaryKey(admin);
    }

    @Override
    public Admin status(Admin admin) {
        Admin admin1 = new Admin();
        if (admin.getStatus().equals("1")){
            admin.setStatus("0");
            adminMapper.updateByPrimaryKeySelective(admin);
            admin1=adminMapper.selectByPrimaryKey(admin);
            return admin1;
        }
        if (admin.getStatus().equals("0")){
            admin.setStatus("1");
            adminMapper.updateByPrimaryKeySelective(admin);
            admin1=adminMapper.selectByPrimaryKey(admin);
        }
        return admin1;
    }
}
