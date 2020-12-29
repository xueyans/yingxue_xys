package com.baizhi.service;

import com.baizhi.entity.Admin;
import java.util.HashMap;

public interface AdminService {
    Admin getAdmin(Admin admin);

    HashMap<String,Object> queryAdminPage(Integer page, Integer rows);

    String add(Admin admin);

    void edit(Admin admin);

    void del(Admin admin);

    Admin status(Admin admin);
}
