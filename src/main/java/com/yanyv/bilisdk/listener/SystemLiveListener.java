package com.yanyv.bilisdk.listener;

import com.alibaba.fastjson.JSONObject;
import com.yanyv.bilisdk.BiliLive;
import com.yanyv.bilisdk.manager.HeadManager;
import com.yanyv.bilisdk.manager.UserDataManager;
import com.yanyv.bilisdk.vo.BiliUser;

/**
 * 负责维护BiliLive对象中的UserMap
 */
public class SystemLiveListener extends LiveAdapter {

    public SystemLiveListener(BiliLive biliLive) {
        super(biliLive);
    }

    @Override
    public void onReceivedComment(Long uid, String userName, String face, String commentText, JSONObject rawData) {
        this.updateUserInfo(uid, userName, face);
    }

    @Override
    public void onReceivedGift(Long uid, String userName, String face, Long giftId, String giftName, Long giftNum, Long price, Boolean paid, JSONObject rawData) {
        this.updateUserInfo(uid, userName, face);
    }

    /***
     * <p>Method ：updateUserInfo
     * <p>Description : 更新用户信息
     *
     * @param uid      用户id
     * @param userName 用户名
     * @param face     用户头像
     * @author 烟雨平生 yanyvpingsheng@qq.com
     */
    public void updateUserInfo(Long uid, String userName, String face) {
        // 当收到消息时将用户转为user对象放入BiliLive的userMap中
        // 如果不存在则创建一个新的user对象
        if (!getBiliLive().getUserMap().containsKey(uid)) {
            BiliUser biliUser = UserDataManager.isUserExist(uid) ? UserDataManager.getUser(uid) : new BiliUser();
            biliUser.setUid(uid);
            biliUser.setUserName(userName);
            biliUser.setFace(face);
            getBiliLive().getUserMap().put(uid, biliUser);
            UserDataManager.setUserData(biliUser);
        } else {
            // 如果存在则更新user对象的头像
            BiliUser biliUser = getBiliLive().getUserMap().get(uid);
            // 判断是否需要更新
            if (!face.equals(biliUser.getFace())) {
                biliUser.setFace(face);
                UserDataManager.setUserData(biliUser);
            }
        }
        // 该方法会判断是否需要下载
        HeadManager.downloadHead(getBiliLive(), uid, face);
    }

}
