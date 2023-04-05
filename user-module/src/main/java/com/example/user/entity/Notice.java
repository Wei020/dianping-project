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
@TableName("tb_notice")
public class Notice {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long Id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long fromId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long toId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long groupId;

    private String content;

    /*
     * 0:申请加群
     * 1:申请加人
     * */
    private Integer type;

    private LocalDateTime sendTime;

    private LocalDateTime doTime;

    /*
    * 0: 未处理
    * 1: 已通过
    * 2: 已拒绝
    * */
    private Integer state;

}
