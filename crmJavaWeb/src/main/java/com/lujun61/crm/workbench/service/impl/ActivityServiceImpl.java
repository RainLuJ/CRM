package com.lujun61.crm.workbench.service.impl;

import com.lujun61.crm.utils.SqlSessionUtil;
import com.lujun61.crm.vo.PaginationVO;
import com.lujun61.crm.workbench.dao.ActivityDao;
import com.lujun61.crm.workbench.dao.ActivityRemarkDao;
import com.lujun61.crm.workbench.domain.Activity;
import com.lujun61.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private final ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private final ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    //private final UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);


    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map) {
        //取得total：获取到的记录总条数
        int total = activityDao.getTotalByCondition(map);

        //取得dataList：所有记录
        List<Activity> dataList = activityDao.getActivityListByCondition(map);

        //创建一个vo对象，将total和dataList封装到vo中
        PaginationVO<Activity> vo = new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //将vo返回
        return vo;
    }


    /**
     * @param ids 需要删除的市场活动记录的id
     * @return boolean 删除是否成功
     * @description 删除市场活动记录
     * @author Jun Lu
     * @date 2022-04-22 21:17:40
     */
    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;


        /* 查询没风险；删除有风险。所以要对比一下，以确保删除正常，未发生异常 */

        //查询出需要删除的备注的数量
        int count1 = activityRemarkDao.getCountByAids(ids);

        //删除备注，返回受到影响的条数（实际删除的数量）
        int count2 = activityRemarkDao.deleteByAids(ids);

        if (count1 != count2) {

            flag = false;

        }

        //删除市场活动
        int count3 = activityDao.delete(ids);
        if (count3 != ids.length) {

            flag = false;

        }

        return flag;
    }

    /**
     * @description 保存操作
     * @author Jun Lu
     * @date 2022-04-22 21:22:08
     */
    @Override
    public boolean save(Activity a) {
        //return activityDao.save(a) == 1;


        boolean flag = true;

        int count = activityDao.save(a);
        if (count != 1) {

            flag = false;

        }

        return flag;
    }
}
