package com.example.chat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)  //调用set方法后返回当前对象
@TableName("tb_msg")
public class Message {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long Id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long fromId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long toId;

    private String fromNickname;

    private String fromIcon;

    private String content;

    /*
    * 0:文字
    * 1:文件
    * */
    private Integer type;

    private LocalDateTime sendTime;


    private Long chatId;

    private Integer deleteFlag;
}
