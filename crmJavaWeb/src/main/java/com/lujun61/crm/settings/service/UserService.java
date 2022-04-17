package com.lujun61.crm.settings.service;

import com.lujun61.crm.exception.LoginException;
import com.lujun61.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
