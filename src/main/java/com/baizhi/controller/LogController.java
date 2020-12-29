package com.baizhi.controller;

import com.aliyuncs.exceptions.ClientException;
import com.baizhi.entity.Admin;
import com.baizhi.entity.Log;
import com.baizhi.service.AdminService;
import com.baizhi.service.LogService;
import com.baizhi.util.AliyunSendMsgUtil;
import com.baizhi.util.CreateValidateCode;
import com.baizhi.util.Md5Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * @author ：薛岩松
 * @time ：2020/12/18-18:49
 */
@Controller
@RequestMapping("/log")
public class LogController {
    @Resource
    LogService logService;

    @ResponseBody
    @RequestMapping("queryLogPage")
    public HashMap<String, Object> queryUserPage(Integer page, Integer rows){
        return logService.queryLogPage(page, rows);
    }

    @ResponseBody
    @RequestMapping("edit")
    public String edit(Log log, String oper){
        String id =null;
        if(oper.equals("edit")){
            logService.edit(log);
        }
        if(oper.equals("del")){
            logService.del(log);
        }
        return id;
    }

}
