package com.yeah.travel.service.impl;

import com.yeah.travel.dao.UserDao;
import com.yeah.travel.dao.impl.UserDaoImpl;
import com.yeah.travel.domain.User;
import com.yeah.travel.service.UserService;
import com.yeah.travel.util.MailUtils;
import com.yeah.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();
    @Override
    public boolean register(User user) {
        User u = userDao.findByUserName(user.getUsername());
        if(u != null) {
            System.out.println("用户名已经存在了,无法注册");
            return false;
        }
        user.setStatus("N");
        user.setCode(UuidUtil.getUuid());
        System.out.println(user);
        userDao.save(user);

        // 发送邮件进行激活
        String emailContent = "<a href = 'http://localhost:8887/travel/activeUserServlet?code=" + user.getCode() +"'>点击激活你的账号</a>";
        System.out.println(user.getEmail());
        System.out.println(emailContent);
        MailUtils.sendMail(user.getEmail(),emailContent,"激活邮件");
        return true;
    }

    @Override
    public boolean active(String code) {
        User u = userDao.findByCode(code);
        if(u != null) {
            userDao.updateStatus(u);
            return true;
        }
        return false;
    }

    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
}
