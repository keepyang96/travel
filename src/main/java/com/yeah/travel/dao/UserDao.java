package com.yeah.travel.dao;

import com.yeah.travel.domain.User;

public interface UserDao {
    User findByUserName(String usename);
    void save(User user);
    User findByCode(String code);
    void updateStatus(User u);
    User findByUsernameAndPassword(String username,String password);
}
