package com.lujun61.crm.workbench.web.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/settings/user/login.do".equals(path)) {
            login(req, resp);
        } else if ("/settings/user/l.do".equals(path)) {

        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) {

    }
}
