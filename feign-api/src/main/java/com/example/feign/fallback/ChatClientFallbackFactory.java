package com.example.feign.fallback;

import com.example.feign.clients.ChatClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatClientFallbackFactory implements FallbackFactory<ChatClient> {
    @Override
    public ChatClient create(Throwable throwable) {
        return null;
    }
}
