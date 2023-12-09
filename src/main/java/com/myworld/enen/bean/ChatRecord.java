package com.myworld.enen.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @ClassName GPTModel
 * @Author wkz
 * @Date 2023/12/5 11:47
 * @Version 1.0
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document("gptRecord")
public class ChatRecord {
    @Id
    private String id;
    @Field
    private String question;
    @Field
    private String answer;
    @Field
    private String publishIp;//咨询问题的人的ip
    @Field
    private String createTime;
}
