package com.tedu.sp01.service;

import com.tedu.sp01.pojo.Item;
import java.util.List;

public interface ItemService
{
    List<Item> getItems(final String p0);
    
    void decreaseNumbers(final List<Item> p0);
}
