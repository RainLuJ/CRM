package com.lujun61.crm.settings.web.controller;

import com.lujun61.crm.commons.contants.Contants;
import com.lujun61.crm.commons.domain.ReturnObject;
import com.lujun61.crm.commons.utils.DateUtils;
import com.lujun61.crm.commons.utils.MD5Util;
import com.lujun61.crm.settings.domain.User;
import com.lujun61.crm.settings.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description 系统权限模块>用户控制器
 * @author Jun Lu
 * @date 2022-05-05 18:48:46
 */
@Controller
public class UserController {
    @Resource
    UserService userService;

    @RequestMapping(value = "settings/qx/user/toLogin.do", method = RequestMethod.GET)
    public String toLogin() {
        return "settings/qx/user/login";
    }


    @RequestMapping("/settings/qx/user/login.do")
    public @ResponseBody
    Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response, HttpSession session){
        // 获取加密算法加密之后的密码（MD5）
        String codeP = MD5Util.getMD5(loginPwd);

        //封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", codeP);
        //调用service层方法，查询用户
        User user=userService.queryUserByLoginActAndPwd(map);

        //根据查询结果，生成响应信息
        ReturnObject returnObject=new ReturnObject();
        if(user==null){
            //登录失败,用户名或者密码错误
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或者密码错误");
        }else{//进一步判断账号是否合法
            //user.getExpireTime()   //2019-10-20
            //        new Date()     //2020-09-10
            if(DateUtils.formateDateTime(new Date()).compareTo(user.getExpiretime())>0){
                //登录失败，账号已过期
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已过期");
            }else if("0".equals(user.getLockstate())){
                //登录失败，状态被锁定
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("状态被锁定");
            }else if(!user.getAllowips().contains(request.getRemoteAddr())){
                //登录失败，ip受限
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("ip受限");
            }else{
                //登录成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

                //把user保存到session中
                session.setAttribute(Contants.SESSION_USER,user);

                //如果需要记住密码，则往外写cookie
                if("true".equals(isRemPwd)){
                    Cookie c1=new Cookie("loginAct",user.getLoginact());
                    c1.setMaxAge(10*24*60*60);
                    response.addCookie(c1);
                    Cookie c2=new Cookie("loginPwd",user.getLoginpwd());
                    c2.setMaxAge(10*24*60*60);
                    response.addCookie(c2);
                }else{
                    //把没有过期cookie删除
                    Cookie c1=new Cookie("loginAct","1");
                    c1.setMaxAge(0);
                    response.addCookie(c1);
                    Cookie c2=new Cookie("loginPwd","1");
                    c2.setMaxAge(0);
                    response.addCookie(c2);
                }
            }
        }

        return returnObject;
    }

/*    @RequestMapping(value = "/settings/qx/user/login.do", method = RequestMethod.POST)
    @ResponseBody
    *//* 返回JSON数据 *//*
    public Object login(String loginAct, String loginPwd, String isRemPwd,
                        HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        // 获取加密算法加密之后的密码（MD5）
        String codeP = MD5Util.getMD5(loginPwd);

        //封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", codeP);
        //调用service层方法，查询用户
        User user = userService.queryUserByLoginActAndPwd(map);

        //根据查询结果，生成响应信息
        ReturnObject returnObject = new ReturnObject();
        if (user == null) {
            //登录失败,用户名或者密码错误
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或者密码错误");
        } else {//进一步判断账号是否合法
            //user.getExpireTime()   //2019-10-20
            //        new Date()     //2020-09-10
            if (DateUtils.formateDateTime(new Date()).compareTo(user.getExpiretime()) > 0) {
                //登录失败，账号已过期
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已过期");
            } else if ("0".equals(user.getLockstate())) {
                //登录失败，状态被锁定
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("状态被锁定");
            } else if (!user.getAllowips().contains(request.getRemoteAddr())) {
                //登录失败，ip受限
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("ip受限");
            } else {
                //登录成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
*//*
                //把user保存到session中
                session.setAttribute(Contants.SESSION_USER, user);

                //如果需要记住密码，则往外写cookie
                if ("true".equals(isRemPwd)) {
                    Cookie c1 = new Cookie("loginAct", user.getLoginact());
                    c1.setMaxAge(10 * 24 * 60 * 60);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd", user.getLoginpwd());
                    c2.setMaxAge(10 * 24 * 60 * 60);
                    response.addCookie(c2);
                } else {
                    //把没有过期cookie删除
                    Cookie c1 = new Cookie("loginAct", "1");
                    c1.setMaxAge(0);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd", "1");
                    c2.setMaxAge(0);
                    response.addCookie(c2);
                }*//*
            }
        }

        return returnObject;
    }*/
}
