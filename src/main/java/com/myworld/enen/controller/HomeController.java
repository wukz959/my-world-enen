package com.myworld.enen.controller;

import com.myworld.enen.Service.HomeService;
import com.myworld.enen.bean.Barrage;
import com.myworld.enen.dto.BarrageDto;
import com.myworld.enen.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName HomeController
 * @Descripton TODO
 * @Author wkz
 * @Date 2023/11/23 20:25
 * @Version 1.0
 */
@RestController
@Slf4j
public class HomeController {
    @Autowired
    private HomeService homeService;

    @GetMapping("/getBarrage")
    public List<BarrageDto> getBarrage(){
        List<BarrageDto> barrageDtoList = homeService.getBarrage();
        return barrageDtoList;
    }

    @PostMapping("/saveBarrage")
    public Result saveBarrage(@Valid @RequestBody Barrage barrage, BindingResult result, HttpServletRequest request){
        if (result.hasErrors()){
            HashMap<String, String> errMap = new HashMap<>();
            result.getFieldErrors().forEach( item -> {
                errMap.put(item.getField(), item.getDefaultMessage());
            });
            System.out.println(errMap);//{barrageContent=不能为null}
            return Result.error(400, "提交的数据不合法").put("data",errMap);
        }
        homeService.saveBarrage(barrage, request);
        return Result.success();
    }
}
