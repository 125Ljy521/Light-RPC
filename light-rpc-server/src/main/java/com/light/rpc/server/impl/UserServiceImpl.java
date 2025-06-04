package com.light.rpc.server.impl;

import com.light.rpc.api.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public String getUserInfo(String userId) {
        return "用户 [" + userId + "] 的信息：VIP 会员";
    }
}
