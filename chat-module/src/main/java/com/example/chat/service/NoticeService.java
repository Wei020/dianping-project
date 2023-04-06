package com.example.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.chat.dto.NoticeDTO;
import com.example.chat.entity.Notice;

import java.util.List;

public interface NoticeService extends IService<Notice> {

    List<NoticeDTO> queryNotice(Long id);

    void doNotice(Long id, Integer flag);
}
