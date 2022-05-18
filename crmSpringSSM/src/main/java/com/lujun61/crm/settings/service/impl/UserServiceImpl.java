package com.lujun61.crm.settings.service.impl;

import com.lujun61.crm.settings.domain.User;
import com.lujun61.crm.settings.mapper.UserMapper;
import com.lujun61.crm.settings.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;

    @Override
    public User queryUserByLoginActAndPwd(Map<String, Object> map) {
        return userMapper.queryUserByLoginActAndPwd(map);
    }
}
