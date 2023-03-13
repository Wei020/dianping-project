package com.example.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)  //调用set方法后返回当前对象
@TableName("tb_chat")
public class Chat {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private Long fromId;


    private Long toId;

    /*
    * 0:群聊
    * 1:私聊
    * */
    private Integer type;
}
