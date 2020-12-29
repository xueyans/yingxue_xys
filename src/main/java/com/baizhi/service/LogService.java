package com.baizhi.service;


import com.baizhi.entity.Log;
import java.util.HashMap;

public interface LogService {

    HashMap<String,Object> queryLogPage(Integer page, Integer rows);

    void edit(Log log);

    void del(Log log);

}
