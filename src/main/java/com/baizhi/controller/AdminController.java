package com.baizhi.controller;

import com.aliyuncs.exceptions.ClientException;
import com.baizhi.entity.Admin;
import com.baizhi.entity.User;
import com.baizhi.service.AdminService;
import com.baizhi.util.AliyunSendMsgUtil;
import com.baizhi.util.CreateValidateCode;
import com.baizhi.util.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/admin")
public class AdminController {
    @Resource
    AdminService as;

    @RequestMapping("/login")
    public Object login(Admin admin,String enCode ,HttpSession session){
        Admin a = as.getAdmin(admin);
        String code= (String) session.getAttribute("code");
        String password= Md5Utils.getMd5Code(a.getSalt()+admin.getPassword());
        try{
            if (a.getStatus().equals("1")) {
                if (enCode.equals(code)) {
                    if (a == null) {
                        throw new RuntimeException("没有该用户");
                    } else if (password.equals(a.getPassword())) {
                        session.setAttribute("admin", a);
                    } else {
                        throw new RuntimeException("密码错误！！！");
                    }
                } else {
                    throw new RuntimeException("验证码错误");
                }
            }else {
                throw new RuntimeException("该账户已被冻结");
            }
            return "redirect:/main/main.jsp";
        }catch (Exception e){
            e.printStackTrace();
            session.setAttribute("message",e.getMessage());
            return "redirect:/login/login.jsp";
        }

    }

    @ResponseBody
    @RequestMapping("/sendMsg")
    public  String sendMsg(String phone) throws ClientException {
        String random=AliyunSendMsgUtil.getRandom(6);
        String message= AliyunSendMsgUtil.sendPhoneCode(phone,random);
        System.out.println(message);
        return message;
    }

    @RequestMapping("/exit")
    public String exit(HttpSession session){
        session.removeAttribute("admin");
        return "redirect:/login/login.jsp";
    }

    @RequestMapping("/Image")
    // 生成验证码
    public String image( HttpSession session, OutputStream os ) throws Exception {
        //1. 获取随机数 验证码
        CreateValidateCode cvc = new CreateValidateCode();
        String code= cvc.getCode();
        System.out.println(code);
        //2. 将验证码存入session作用域
        session.setAttribute("code",code);
        //3. 通过输出流输出验证码图片
        cvc.write(os);
        return null;
    }

    @ResponseBody
    @RequestMapping("queryAdminPage")
    public HashMap<String, Object> queryUserPage(Integer page, Integer rows){
        return as.queryAdminPage(page, rows);
    }

    @ResponseBody
    @RequestMapping("edit")
    public String edit(Admin admin, String oper){
        String id =null;
        if(oper.equals("add")){
            id = as.add(admin);
        }
        if(oper.equals("edit")){
            as.edit(admin);
        }
        if(oper.equals("del")){
            as.del(admin);
        }
        return id;
    }

    @ResponseBody
    @RequestMapping("status")
    public Admin status(Admin admin){
        Admin admin1= as.status(admin);
        return admin1;
    }

}
