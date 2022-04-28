package com.lujun61.crm.workbench.web.controller;

import com.lujun61.crm.settings.domain.User;
import com.lujun61.crm.settings.service.UserService;
import com.lujun61.crm.settings.service.impl.UserServiceImpl;
import com.lujun61.crm.utils.DateTimeUtil;
import com.lujun61.crm.utils.PrintJson;
import com.lujun61.crm.utils.ServiceFactory;
import com.lujun61.crm.utils.UUIDUtil;
import com.lujun61.crm.vo.PaginationVO;
import com.lujun61.crm.workbench.domain.Activity;
import com.lujun61.crm.workbench.domain.ActivityRemark;
import com.lujun61.crm.workbench.service.ActivityService;
import com.lujun61.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/workbench/activity/getUserList.do".equals(path)) {
            getUserList(req, resp);
        } else if ("/workbench/activity/save.do".equals(path)) {
            save(req, resp);
        } else if ("/workbench/activity/pageList.do".equals(path)) {
            pageList(req, resp);
        } else if ("/workbench/activity/delete.do".equals(path)) {
            delete(req, resp);
        } else if ("/workbench/activity/getUserListAndActivity.do".equals(path)) {
            getUserListAndActivity(req, resp);
        } else if ("/workbench/activity/update.do".equals(path)) {
            update(req, resp);
        } else if ("/workbench/activity/detail.do".equals(path)) {
            detail(req, resp);
        } else if ("/workbench/activity/getRemarkListByAid.do".equals(path)) {
            getRemarkListByAid(req, resp);
        } else if ("/workbench/activity/deleteRemark.do".equals(path)) {
            deleteRemark(req, resp);
        }
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.deleteRemarkByArId(id);

        PrintJson.printJsonFlag(response, flag);
    }

    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {
        String activityId = request.getParameter("activityId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<ActivityRemark> asRemarkList = as.getRemarkListByAid(activityId);

        PrintJson.printJsonObj(response, asRemarkList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity a = as.detail(id);

        // 优先添加至小作用域
        request.setAttribute("a", a);

        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request, response);

    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        //创建时间：当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        //创建人：当前登录用户
        String editBy = ((User) request.getSession().getAttribute("user")).getName();

        Activity a = new Activity();
        a.setId(id);
        a.setCost(cost);
        a.setStartDate(startDate);
        a.setOwner(owner);
        a.setName(name);
        a.setEndDate(endDate);
        a.setDescription(description);
        a.setCreateTime(editTime);
        a.setCreateBy(editBy);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.edit(a);

        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserListAndActivity(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

         /*

            总结：
                controller调用service的方法，返回值应该是什么
                你得想一想前端要什么，就要从service层取什么

            前端需要的，管业务层去要
            uList
            a

            以上两项信息，复用率不高，我们选择使用map打包这两项信息即可
            map

         */

        Map<String, Object> list = as.getUserListAndActivity(id);

        PrintJson.printJsonObj(resp, list);
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) {
        String[] ids = req.getParameterValues("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean isSuccess = as.delete(ids);

        PrintJson.printJsonFlag(resp, isSuccess);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        // 页码（第几页）
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.parseInt(pageNoStr);

        //每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.parseInt(pageSizeStr);

        //计算出略过的记录数（使用公式）
        int skipCount = (pageNo - 1) * pageSize;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /*

            前端要： 市场活动信息列表
                    查询的总条数

                    业务层拿到了以上两项信息之后，如果做返回
                    map
                    map.put("dataList":dataList)
                    map.put("total":total)
                    PrintJSON map --> json
                    {"total":100,"dataList":[{市场活动1},{2},{3}]}


                    vo
                    PaginationVO<T>
                        private int total;
                        private List<T> dataList;

                    PaginationVO<Activity> vo = new PaginationVO<>;
                    vo.setTotal(total);
                    vo.setDataList(dataList);
                    PrintJSON vo --> json
                    {"total":100,"dataList":[{市场活动1},{2},{3}]}


                    将来分页查询，每个模块都有，所以我们选择使用一个通用vo，操作起来比较方便




         */
        PaginationVO<Activity> vo = as.pageList(map);

        //vo--> {"total":100,"dataList":[{市场活动1},{2},{3}]}
        PrintJson.printJsonObj(response, vo);

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
        String createBy = ((User) request.getSession().getAttribute("user")).getName();

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
