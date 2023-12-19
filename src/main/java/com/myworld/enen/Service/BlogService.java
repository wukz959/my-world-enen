package com.myworld.enen.Service;

import com.myworld.enen.bean.Blog;
import com.myworld.enen.constants.Constants;
import com.myworld.enen.utils.Result;
import com.myworld.enen.utils.UploadOSSFile;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.List;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

import static com.myworld.enen.constants.Constants.BLOG_SAVE_DIRECTORY;

/**
 * @ClassName BlogService
 * @Author wkz
 * @Date 2023/12/4 9:48
 * @Version 1.0
 */
@Service
@Slf4j
public class BlogService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    public List<Blog> getAllBlogList()
    {
        Criteria criteria = Criteria.where("hadDeleted").is(false).and("types").is("BLOG");
        List<Blog> blogs = mongoTemplate.find(Query.query(criteria).with(Sort.by(Sort.Order.asc("updateTime"))), Blog.class);
        return blogs;
    }

    public Result saveBlog(Blog blog) {
        // xss攻击防范的这些不写
        String htmlContent = blog.getContent();
        blog.setDigest(extractTextFromHTML(htmlContent, 150));
        Document document = Jsoup.parse(htmlContent);
        Elements imgElements = document.select("img");
        // 下载图片上传到服务器并将img的src指向为服务器的图片链接
        for (Element img : imgElements) {
            String src = img.attr("src");
            String suffix = src.substring(src.lastIndexOf('.')-1); //.png
            String imgName = Constants.BLOG_IMG_PREFIX + generateEasyImgName() + suffix;
            // https://hellowukz.oss-cn-guangzhou.aliyuncs.com/Blogs/img-1702884754177-12120.png
            try (InputStream in = new URL(src).openStream()){ // try-with-resources: can auto close in stream
                UploadOSSFile.upload(in,imgName,BLOG_SAVE_DIRECTORY,endpoint,accessKeyId,accessKeySecret,bucketName);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
            String newImageUrl = Constants.BLOG_IMG_PATH_PREFIX + BLOG_SAVE_DIRECTORY + imgName;
            img.attr("src", newImageUrl);
        }
        blog.setContent(document.body().html());
        mongoTemplate.save(blog);
        return Result.success();
    }

    public List<Blog> getOpenSource() {
        Criteria criteria = Criteria.where("hadDeleted").is(false).and("types").is("OPEN_SOURCE");
        List<Blog> openSource = mongoTemplate.find(Query.query(criteria), Blog.class);
        return openSource;
    }

    public static String extractTextFromHTML(String htmlString, int maxLength) {
        Document doc = Jsoup.parse(htmlString);
        String textContent = doc.text();

        if (textContent.length() > maxLength) {
            textContent = textContent.substring(0, maxLength) + "...";
        }

        return textContent;
    }

    public String generateEasyImgName(){
        // 获取当前时间戳
        long currentTimeMillis = System.currentTimeMillis();
        // 生成一个1000到9999之间的随机数
        int randomFourDigits = ThreadLocalRandom.current().nextInt(1000, 10000);
        // 将时间戳和随机数拼接成字符串
        return currentTimeMillis + "-" + randomFourDigits;
    }
}
