package com.lij.myqqrobotserver.common.consts;

import java.net.InetSocketAddress;
import java.net.Proxy;

import com.alibaba.fastjson.JSONArray;

public class Const {
    public static JSONArray fortunes = null;
    public static JSONArray groupList = null;
    public static JSONArray friendList = null;
    public static Proxy pixivProxy=new Proxy(Proxy.Type.HTTP,new InetSocketAddress("127.0.0.1",10809));
    
}
