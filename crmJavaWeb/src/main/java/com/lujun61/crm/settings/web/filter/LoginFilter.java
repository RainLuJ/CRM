package com.lujun61.crm.settings.web.filter;


import com.lujun61.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class LoginFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        //System.out.println("进入到验证有没有登录过的过滤器");

        // 想使用子类HttpServletRequest中的方法，就需要进行强转
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String path = request.getServletPath();

        //不应该被拦截的资源，自动放行请求
        /**
         * 需要给用户登录的机会，再进行判断：用户是否在 登录之后 再访问其它后台资源
         */
        /*   /login.jsp 浏览器（用户访问的地址） |||||  /settings/user/login.do 服务器内部资源互相访问的路径 */
        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)) {

            chain.doFilter(req, resp);


            //其他资源必须验证有没有登录过
        } else {

            /*
                关于session的问题

                如果是正常登录，账号密码验证成功后会由servlet来创建一个session对象，并把session对象的id通过cookie发送给浏览器。
                session的创建，有两个语句 一个是 request.getSession（false），request.getSession（true）。
                ture那个呢，就是查询你有没有Session对象，如果没有的话就为你创建一个。
                而另一个false的，就是查询你有没有Session对象，如果没有的话就返回一个null，不会为你创建。
                所以老杨当时就是在拦截器里面写false那个语句，来验证是否是恶意登录。拦截器部分就是这一点和这节课不同。
                但是我按照老杨的方法，每次恶意登录都能成功进入到workbench的页面。我f12，发现我没有登录，cookie上也有session的id。
                我以为是我的cookie没有清理，结果我清了cookie还是一样结果。后来我查阅资料发现，原来jsp会自动帮你创建session。
                这也是为什么老师是查询session里面的值而不是查session有没有。
                如果想要关掉jsp自动创建session，只要加上 session="false"就行了

             */
            HttpSession session = request.getSession();


            User user = (User) session.getAttribute("user");

            //如果user不为null，说明登录过
            if (user != null) {

                chain.doFilter(req, resp);

                //没有登录过
            } else {

                //重定向到登录页
            /*

                重定向的路径怎么写？
                在实际项目开发中，对于路径的使用，不论操作的是前端还是后端，应该一律使用绝对路径
                 关于转发和重定向的路径的写法如下：
                 转发：
                    使用的是一种特殊的绝对路径的使用方式，这种绝对路径前面不加/项目名，这种路径也称之为【内部路径】
                    /login.jsp
                 重定向：
                    使用的是传统绝对路径的写法，前面必须以/项目名开头，后面跟具体的资源路径
                    /crm/login.jsp


                为什么使用重定向，使用转发不行吗？
                   转发之后，路径会停留在老路径上，而不是跳转之后最新资源的路径
                   我们应该在为用户跳转到登录页的同时，将浏览器的地址栏应该自动设置为当前的登录页的路径


             */
                response.sendRedirect(request.getContextPath() + "/login.jsp");

            }


        }


    }
}
