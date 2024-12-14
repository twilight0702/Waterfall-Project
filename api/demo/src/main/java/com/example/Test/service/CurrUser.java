package com.example.Test.service;

import java.util.HashMap;
import java.util.Map;

public class CurrUser {
    private final static Map<Integer, String> currUsers = new HashMap<>();

    public static void addUser(Integer key, String name) {
        currUsers.put(key, name);
        System.out.println("添加用户成功:" + key + "   " + currUsers);
    }

    public static String findUser(Integer key) {
        String result = currUsers.get(key);
        if(result==null)
        {
            return "-114514";
        }
        System.out.println("查找用户:" + key + "   " + result);
        return result;
    }
    public static void removeUser(Integer key) {
        currUsers.remove(key);
        System.out.println("删除用户:" + key + "   " + currUsers);
    }
}
