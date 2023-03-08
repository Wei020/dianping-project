package com.example.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)  //调用set方法后返回当前对象
@TableName("tb_msg")
public class Message {

    private Long Id;

    private Long fromId;

    private Long toId;

    private String content;

    /*
    * 0:文字
    * 1:文件
    * */
    private Integer type;

    private LocalDateTime sendTime;

    private String fromNickname;

    private String fromIcon;

    private String toNickname;

    private String toIcon;

    private Long chatId;
}
