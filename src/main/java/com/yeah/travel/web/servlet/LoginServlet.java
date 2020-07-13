package com.yeah.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeah.travel.domain.ResultInfo;
import com.yeah.travel.domain.User;
import com.yeah.travel.service.UserService;
import com.yeah.travel.service.impl.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> parameterMap = req.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        UserService userService = new UserServiceImpl();
        User u = userService.login(user);

        // 使用异步
        ResultInfo resultInfo = new ResultInfo();
        ObjectMapper mapper = new ObjectMapper();
        //String json = null;
        if(u == null) {
            // 用户名密码错误
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("用户名密码错误");
        }
        if(u != null && !"Y".equals(u.getStatus())) {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("邮箱尚未激活请登录邮箱进行激活");
        }
        if(u != null && "Y".equals(u.getStatus())) {
            req.getSession().setAttribute("user",u);
            resultInfo.setFlag(true);
        }
        //json = mapper.writeValueAsString(resultInfo);
        resp.setContentType("application/json;charset=utf-8");
        //resp.getWriter().write(json);
        mapper.writeValue(resp.getOutputStream(),resultInfo);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
