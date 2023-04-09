package com.example.feign.fallback;

import com.example.feign.clients.UserClient;
import com.example.feign.dto.Result;
import com.example.feign.dto.UserListDTO;
import com.example.feign.entity.User;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable throwable) {
        return new UserClient() {

            @Override
            public Result queryUserById(Long id) {
                return null;
            }

            @Override
            public Result queryUserList(UserListDTO userListDTO) {
                return null;
            }

            @Override
            public Result followUser(Long id, Long followId) {
                return null;
            }

            @Override
            public Result notFollowUser(Long id, Long followId) {
                return null;
            }

        };
    }
}
