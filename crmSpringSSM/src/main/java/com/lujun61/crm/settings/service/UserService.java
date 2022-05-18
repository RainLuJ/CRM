package com.lujun61.crm.settings.service;

import com.lujun61.crm.settings.domain.User;

import java.util.Map;

public interface UserService {
    User queryUserByLoginActAndPwd(Map<String, Object> map);
}
