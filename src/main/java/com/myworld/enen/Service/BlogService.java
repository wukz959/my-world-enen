package com.myworld.enen.Service;

import com.myworld.enen.bean.Blog;
import com.myworld.enen.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName BlogService
 * @Author wkz
 * @Date 2023/12/4 9:48
 * @Version 1.0
 */
@Service
public class BlogService {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Blog> getAllBlogList()
    {
        Criteria criteria = Criteria.where("hadDeleted").is(false).and("types").is("BLOG");
        List<Blog> blogs = mongoTemplate.find(Query.query(criteria).with(Sort.by(Sort.Order.asc("updateTime"))), Blog.class);
        return blogs;
    }

    public Result saveBlog(Blog blog) {
        // xss攻击防范的这些不写
        System.out.println(blog);
        mongoTemplate.save(blog);
        return Result.success();
    }

    public List<Blog> getOpenSource() {
        Criteria criteria = Criteria.where("hadDeleted").is(false).and("types").is("OPEN_SOURCE");
        List<Blog> openSource = mongoTemplate.find(Query.query(criteria), Blog.class);
        return openSource;
    }
}
