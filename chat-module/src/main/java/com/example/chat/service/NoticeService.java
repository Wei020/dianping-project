package com.example.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.chat.dto.NoticeDTO;
import com.example.chat.entity.Notice;

import java.util.List;

public interface NoticeService extends IService<Notice> {

    List<NoticeDTO> queryNotice(Long id, Integer current);

    void doNotice(Long id, Integer flag);

    void notice(NoticeDTO noticeDTO);

    Integer queryNoticeNum();
}
