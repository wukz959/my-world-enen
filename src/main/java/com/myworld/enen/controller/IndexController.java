package com.myworld.enen.controller;

import com.myworld.enen.bean.Barrage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName IndexController
 * @Descripton TODO
 * @Author wkz
 * @Date 2023/11/15 20:56
 * @Version 1.0
 */
@RestController
@Slf4j
public class IndexController {
    @Autowired
    private MongoTemplate mongoTemplate;
//    @Value("${myGPT.url}")
//    private String hhh;
    @RequestMapping("/hello")
    public String sayHello(){
        log.debug("debug...........");
        log.info("info.............");
        log.error("error...........");
        log.warn("warn.............");
        log.trace("trace.............");
        return "Hello";
    }

//    @RequestMapping("/testInsert")
//    public String testInsert(){
//        Date date = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        Barrage barrage = new Barrage("我是弹幕", dateFormat.format(date), null, null);
//        Barrage res = mongoTemplate.insert(barrage);
//        return res.toString();
//    }
}
