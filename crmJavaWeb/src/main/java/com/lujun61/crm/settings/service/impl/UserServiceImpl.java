package com.lujun61.crm.settings.service.impl;

import com.lujun61.crm.exception.LoginException;
import com.lujun61.crm.settings.dao.UserDao;
import com.lujun61.crm.settings.domain.User;
import com.lujun61.crm.settings.service.UserService;
import com.lujun61.crm.utils.DateTimeUtil;
import com.lujun61.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String, String> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);

        User user = userDao.login(map);

        if (user == null) {
            throw new LoginException("用户名或密码错误");
        }

        String allowIps = user.getAllowIps();
        String expireTime = user.getExpireTime();
        String lockState = user.getLockState();
        if (!allowIps.contains(ip)) {
            throw new LoginException("ip地址受限");
        }

        if (expireTime.compareTo(DateTimeUtil.getSysTime()) < 0) {
            throw new LoginException("访问超时");
        }

        if ("0".equals(lockState)) {
            throw new LoginException("无权访问");
        }

        return user;
    }

    @Override
    public List<User> getUserList() {
        return userDao.getUserList();
    }
}
