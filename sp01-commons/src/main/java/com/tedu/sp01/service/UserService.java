package com.tedu.sp01.service;

import com.tedu.sp01.pojo.User;

public interface UserService
{
    User getUser(final Integer p0);
    
    void addScore(final Integer p0, final Integer p1);
}
