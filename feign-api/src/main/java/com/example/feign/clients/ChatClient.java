package com.example.feign.clients;

import com.example.feign.dto.NoticeDTO;
import com.example.feign.dto.Result;
import com.example.feign.fallback.ChatClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "chatservice", fallbackFactory = ChatClientFallbackFactory.class)
public interface ChatClient {

    @PostMapping("/notice")
    Result notice(@RequestBody NoticeDTO noticeDTO);
}
