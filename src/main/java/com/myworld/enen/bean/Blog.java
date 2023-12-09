package com.myworld.enen.bean;

import com.myworld.enen.types.BlogTypes;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;

/**
 * @ClassName Blog
 * @Author wkz
 * @Date 2023/12/3 16:06
 * @Version 1.0
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("blogs")
public class Blog {
    @Id
    private String id;
    @Field
    @NotNull
    private String title;
    @Field
    @NotNull
    private String content;
    @Field
    @Builder.Default
    private Double version = 1.0;//文章版本
    @Field
    private String digest;// 摘要
    @Field
    private BlogTypes types;// 文章类型：开源作品/个人博客
    @Field
    private String createTime;
    @Field
    @NotNull
    private String updateTime;
    @Field
    @Builder.Default
    private Boolean hadDeleted = false;
}
