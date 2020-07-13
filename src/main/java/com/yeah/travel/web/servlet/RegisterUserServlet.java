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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/registerUserServlet")
public class RegisterUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResultInfo resultInfo = new ResultInfo();
        ObjectMapper mapper = new ObjectMapper();
        String json;

        // 校验验证码
        String check = req.getParameter("check");
        // 从session中获取验证嘛
        HttpSession session = req.getSession();
        String checkcode_server = (String)session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER"); // 保证验证码只能使用一次

        if(checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码错误");
            json = mapper.writeValueAsString(resultInfo);
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write(json);
            return;
        }

        resultInfo = new ResultInfo();
        mapper = new ObjectMapper();
        Map<String, String[]> parameters = req.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user,parameters);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        UserService userService = new UserServiceImpl();
        boolean flag = userService.register(user);

        resultInfo.setFlag(flag);
        if(flag) {
            resultInfo.setData(user);
        } else {
            resultInfo.setErrorMsg("注册失败");
        }
        // info 对象序列化成json
        mapper = new ObjectMapper();
        json = mapper.writeValueAsString(resultInfo);
        // 将json 数据写回客户端
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(json);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}

