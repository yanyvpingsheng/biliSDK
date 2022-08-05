package com.yanyv.bilisdk.vo;

import com.alibaba.fastjson.JSONObject;

/**
 * <p>BiliUser</p>
 * <p>用户信息</p>
 * @author 烟雨平生
 */
public class BiliUser {
    // 用户uid
    private Long uid;
    // 用户昵称
    private String userName;
    // 用户头像
    private String face;
    // 头像下载状态
    private Boolean downloaded = false;
    // 用户大航海等级
    private Integer guardLevel;
    // 用户积分 高能榜会污染此属性 不要用
    private Integer score;
    // 游戏积分
    private Integer gameScore = 0;

    /**
     * <p>Method ：BiliUser
     * <p>Description : 全参构造方法
     *
     * @param uid          用户id
     * @param uname        用户昵称
     * @param face         用户头像
     * @param guardLevel   用户大航海等级
     * @param score        用户积分
     * @author 烟雨平生 yanyvpingsheng@qq.com
     */
    public BiliUser(Long uid, String uname, String face, Integer guardLevel, Integer score) {
        this.uid = uid;
        this.userName = uname;
        this.face = face;
        this.guardLevel = guardLevel;
        this.score = score;
    }

    /**
     * <p>Method ：BiliUser
     * <p>Description : 无参构造方法
     *
     * @author 烟雨平生 yanyvpingsheng@qq.com
     */
    public BiliUser() {
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public Integer getGuardLevel() {
        return guardLevel;
    }

    public void setGuardLevel(Integer guardLevel) {
        this.guardLevel = guardLevel;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(Boolean downloaded) {
        this.downloaded = downloaded;
    }

    public Integer getGameScore() {
        return gameScore;
    }

    public void setGameScore(Integer gameScore) {
        this.gameScore = gameScore;
    }

    /**
     * <p>Method ：addGameScore
     * <p>Description : 增加游戏积分
     *
     * @param add 增加的游戏积分
     * @author 烟雨平生 yanyvpingsheng@qq.com
     */
    public void addGameScore(Integer add) {
        this.gameScore += add;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uid", uid);
        jsonObject.put("userName", userName);
        jsonObject.put("face", face);
        jsonObject.put("guardLevel", guardLevel);
        jsonObject.put("score", score);
        jsonObject.put("gameScore", gameScore);
        jsonObject.put("downloaded", downloaded);
        return jsonObject;
    }
}
