package com.lujun61.crm.settings.web.controller;

import com.lujun61.crm.settings.domain.User;
import com.lujun61.crm.settings.service.UserService;
import com.lujun61.crm.settings.service.impl.UserServiceImpl;
import com.lujun61.crm.utils.MD5Util;
import com.lujun61.crm.utils.PrintJson;
import com.lujun61.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/settings/user/login.do".equals(path)) {
            login(req, resp);
        } else if ("/settings/user/l.do".equals(path)) {

        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) {
        String loginAct = req.getParameter("loginAct");
        String loginPwd = req.getParameter("loginPwd");

        // 获取加密算法加密之后的密码（MD5）
        String codeP = MD5Util.getMD5(loginPwd);

        // 获取访问资源的计算机IP
        String remoteAddr = req.getRemoteAddr();


        // 代理的方式获取service对象
        UserService service = (UserService) ServiceFactory.getService(new UserServiceImpl());


        try {
            // 登录操作
            /* 如果存在异常，就执行catch将msg创建 */
            User user = service.login(loginAct, codeP, remoteAddr);

            //能执行到此，说明user正常获取，就将数据存放到session中
            req.getSession().setAttribute("user", user);

            PrintJson.printJsonFlag(resp, true);
        } catch (Exception e) {
            e.printStackTrace();

            //一旦程序执行了catch块的信息，说明业务层为我们验证登录失败，为controller抛出了异常
            //表示登录失败
            /*
                将错误信息装换为JSON格式，打到客户端
                {"success":true,"msg":?}

             */
            String msg = e.getMessage();
            /*

                我们现在作为contoller，需要为ajax请求提供多项信息

                可以有两种手段来处理：
                    （1）将多项信息打包成为map，将map解析为json串
                    （2）创建一个Vo
                            private boolean success;
                            private String msg;

                    使用场景：
                        如果对于展现的信息将来还会大量的使用，我们创建一个vo类，使用方便
                        如果对于展现的信息只有在这个需求中能够使用，我们使用map就可以了


             */
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("msg", msg);
            PrintJson.printJsonObj(resp, map);

        }


    }
}
