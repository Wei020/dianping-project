package com.example.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.chat.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
}
