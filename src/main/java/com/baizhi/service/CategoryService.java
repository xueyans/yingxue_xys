package com.baizhi.service;

import com.baizhi.entity.Category;
import com.baizhi.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public interface CategoryService {

    HashMap<String,Object> queryCatePageOne(Integer page, Integer rows);

    HashMap<String,Object> queryCatePageTwo(Integer page, Integer rows,String categoryId);

    void add(Category category);

    void edit(Category category);

    String del(Category category);



}
