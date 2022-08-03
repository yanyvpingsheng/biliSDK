package com.yanyv.bilisdk.manager;

import com.alibaba.fastjson.JSONObject;
import com.yanyv.bilisdk.vo.BiliUser;

public class UserDataManager {
    // 获取用户数据
    public static JSONObject getUsersData() {
        return ConfigManager.getInstance().getUsersData();
    }
    // 用户是否存在
    public static boolean isUserExist(Long uid) {
        return getUsersData().containsKey(uid.toString());
    }
    // 获取用户
    public static BiliUser getUser(Long uid) {
        return getUsersData().getObject(uid.toString(), BiliUser.class);
    }
    // 获取用户名字
    public static String getUserName(Long uid) {
        return getUser(uid).getUserName();
    }
    // 获取用户头像地址
    public static String getUserFace(Long uid) {
        return getUser(uid).getFace();
    }
    // 存储用户
    public static void setUserData(BiliUser biliUser) {
        getUsersData().put(biliUser.getUid().toString(), biliUser.toJSON());
    }
}
