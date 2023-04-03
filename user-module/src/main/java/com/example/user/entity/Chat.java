package com.example.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)  //调用set方法后返回当前对象
@TableName("tb_chat")
public class Chat {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long fromId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long toId;

    /*
    * 0:群聊
    * 1:私聊
    * */
    private Integer type;

    /*
    * 0: 未通过
    * 1: 已通过
    * */
    private Integer state;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
