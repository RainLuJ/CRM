package com.lujun61.crm.workbench.web.controller;

import com.lujun61.crm.settings.domain.User;
import com.lujun61.crm.settings.service.UserService;
import com.lujun61.crm.settings.service.impl.UserServiceImpl;
import com.lujun61.crm.utils.DateTimeUtil;
import com.lujun61.crm.utils.PrintJson;
import com.lujun61.crm.utils.ServiceFactory;
import com.lujun61.crm.utils.UUIDUtil;
import com.lujun61.crm.workbench.domain.Activity;
import com.lujun61.crm.workbench.service.ActivityService;
import com.lujun61.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/workbench/activity/getUserList.do".equals(path)) {
            getUserList(req, resp);
        } else if ("/workbench/activity/save.do".equals(path)) {
            save(req, resp);
        }
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        //创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人：当前登录用户
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        Activity a = new Activity();
        a.setId(id);
        a.setCost(cost);
        a.setStartDate(startDate);
        a.setOwner(owner);
        a.setName(name);
        a.setEndDate(endDate);
        a.setDescription(description);
        a.setCreateTime(createTime);
        a.setCreateBy(createBy);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.save(a);

        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resp) {
        UserService factory = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> userList = factory.getUserList();

        PrintJson.printJsonObj(resp, userList);
    }
}
