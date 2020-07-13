package com.yeah.travel.service;

import com.yeah.travel.domain.User;

public interface UserService {

    boolean register(User user);

    boolean active(String code);

    User login(User user);
}
