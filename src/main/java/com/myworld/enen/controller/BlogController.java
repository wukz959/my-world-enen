package com.myworld.enen.controller;

import com.myworld.enen.Service.BlogService;
import com.myworld.enen.bean.Blog;
import com.myworld.enen.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName BlogController
 * @Author wkz
 * @Date 2023/12/3 16:06
 * @Version 1.0
 */
@RestController("/myworld")
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Value("${spring.profiles.active}")
    private String active;

    @GetMapping("/getAllBlogs")
    public List<Blog> getBlogs(){
        return blogService.getAllBlogList();
    }

    @PostMapping("/saveBlog")
    public Result save(@RequestBody Blog blog){
        if (active.equals("prod")){
            return Result.error(400,"不允许的操作");
        }
        return blogService.saveBlog(blog);
    }

    @GetMapping("/getOpenSource")
    public List<Blog> getOpenSource(){
        return blogService.getOpenSource();
    }
}
