package com.myworld.enen.bean;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;

/**
 * @ClassName Barrage
 * @Descripton TODO
 * @Author wkz
 * @Date 2023/11/23 19:43
 * @Version 1.0
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("barrage")
public class Barrage {
    @Id
    private String id;
    @Field
    @NotNull
    private String barrageContent;
    @Field
    @NotNull
    private String createTime;
    @Field
    @Builder.Default
    private Boolean hadDeleted = false;
    @Field
    private String publishIp;//发布弹幕的人的ip

    public Barrage(String barrageContent, String createTime, Boolean hadDeleted, String publishIp) {
        this.barrageContent = barrageContent;
        this.createTime = createTime;
        this.hadDeleted = hadDeleted;
        this.publishIp = publishIp;
    }
}
