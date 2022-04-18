package com.lujun61.crm.workbench.service.impl;

import com.lujun61.crm.settings.dao.UserDao;
import com.lujun61.crm.utils.SqlSessionUtil;
import com.lujun61.crm.workbench.dao.ActivityDao;
import com.lujun61.crm.workbench.dao.ActivityRemarkDao;
import com.lujun61.crm.workbench.domain.Activity;
import com.lujun61.crm.workbench.service.ActivityService;

public class ActivityServiceImpl implements ActivityService {
    private final ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private final ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private final UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public boolean save(Activity a) {
        return activityDao.save(a) == 1;
    }
}
