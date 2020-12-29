package com.baizhi.controller;

import com.baizhi.entity.City;
import com.baizhi.entity.Pro;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author EchartsController
 * @time 2020/12/28-10:16
 */
@RestController
@RequestMapping("echarts")
public class EchartsController {

    /*
    {
      "month": ["1月","2月","3月","4月","5月","6月"],
      "boys": [5, 20, 360, 10, 100, 20],
      "girls": [50, 200, 360, 100, 100, 200]
    }
     性别   注册时间
    select concat(month(create_date),'月') month,count(id) count from yx_user  where sex='男' GROUP BY  month(create_date)
    * */
    @RequestMapping("getUserData")
    public HashMap<String, Object> getUserData(){

        HashMap<String, Object> map = new HashMap<>();

        map.put("month", Arrays.asList("1月","2月","3月","4月","5月","6月"));
        map.put("boys",Arrays.asList(5, 20, 360, 10, 100, 20));
        map.put("girls",Arrays.asList(50, 200, 360, 100, 100, 200));
        //http  应用层  发送
        //什么时候发送  轮询  定时
        //http  应用层  短链接
        //tcp/ip 网络层   长链接  一直存在 双向

        return map;
    }

    /*
    *
            [  List
              {   pro
                "sex": "小男孩",
                "citys": [
                  {"name": "北京","value": 31},   City
                  {"name": "河南","value": 53},
                  {"name": "河北","value": 37}
                ]
              },{
                "sex": "小姑娘",
                "citys": [
                  {"name": "陕西","value": 31},
                  {"name": "山西","value": 53},
                  {"name": "山东","value": 37}
                ]
              }
        ]
    * */
    @RequestMapping("getUserChina")
    public ArrayList<Pro> getUserChina(){

        ArrayList<City> boysCityList = new ArrayList<>();
        boysCityList.add(new City("河南",280));
        boysCityList.add(new City("山东",300));
        boysCityList.add(new City("山西",500));
        boysCityList.add(new City("湖南",200));
        boysCityList.add(new City("湖北",100));

        ArrayList<City> girlsCityList = new ArrayList<>();
        girlsCityList.add(new City("河北",280));
        girlsCityList.add(new City("辽宁",400));
        girlsCityList.add(new City("甘肃",600));
        girlsCityList.add(new City("黑龙江",300));
        girlsCityList.add(new City("云南",200));

        ArrayList<Pro> pros = new ArrayList<>();
        pros.add(new Pro("小男孩",boysCityList));
        pros.add(new Pro("小姑娘",girlsCityList));

        return pros;
    }

}
