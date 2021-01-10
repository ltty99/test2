package com.tedu.sp01.service;

import com.tedu.sp01.pojo.Order;

public interface OrderService
{
    Order getOrder(final String p0);
    
    void addOrder(final Order p0);
}
