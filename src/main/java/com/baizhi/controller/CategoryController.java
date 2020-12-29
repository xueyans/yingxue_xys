package com.baizhi.controller;

import com.baizhi.entity.Category;
import com.baizhi.entity.User;
import com.baizhi.service.CategoryService;
import com.baizhi.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * @author UserController
 * @time 2020/12/21-11:16
 */
@Controller
@RequestMapping("category")
public class CategoryController {

    @Resource
    CategoryService categoryService;

    @ResponseBody
    @RequestMapping("queryCatePageOne")
    public HashMap<String, Object> queryUserPageOne(Integer page, Integer rows){
        return categoryService.queryCatePageOne(page,rows);
    }

    @ResponseBody
    @RequestMapping("queryCatePageTwo")
    public HashMap<String, Object> queryUserPageTwo(Integer page, Integer rows,String categoryId){
        return categoryService.queryCatePageTwo(page,rows,categoryId);
    }


    @ResponseBody
    @RequestMapping("edit")
    public void edit(Category category, String oper, HttpSession session){
       String message=null;
        if(oper.equals("add")){
             categoryService.add(category);
        }
        if(oper.equals("edit")){
            categoryService.edit(category);
        }
        if(oper.equals("del")){
            message= categoryService.del(category);
        }
        session.setAttribute("message",message);
    }

}
