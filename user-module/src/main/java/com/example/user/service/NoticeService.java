package com.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.user.dto.NoticeDTO;
import com.example.user.entity.Notice;

import java.util.List;

public interface NoticeService extends IService<Notice> {

    List<NoticeDTO> queryNotice(Long id);

    void doNotice(Long id, Integer flag);
}
