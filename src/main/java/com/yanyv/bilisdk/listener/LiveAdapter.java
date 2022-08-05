package com.yanyv.bilisdk.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yanyv.bilisdk.BiliLive;
import com.yanyv.bilisdk.vo.BiliUser;

import java.util.List;

public class LiveAdapter implements LiveListener {

    private BiliLive biliLive = null;

    // 无参构造方法
    public LiveAdapter() {
    }

    // 传入BiliLive的构造方法
    public LiveAdapter(BiliLive biliLive) {
        this.biliLive = biliLive;
    }

    public BiliLive getBiliLive() {
        return biliLive;
    }

    public void setBiliLive(BiliLive biliLive) {
        this.biliLive = biliLive;
    }

    /**
     * <p>onRoomConnected</p>
     * <p>当弹幕姬与房间连接成功时的回调方法</p>
     *
     * @author 烟雨平生
     */
    @Override
    public void onRoomConnected() {

    }

    /**
     * <p>onRoomDisconnected</p>
     * <p>当弹幕姬与房间断开连接时的回调方法</p>
     *
     * @author 烟雨平生
     */
    @Override
    public void onRoomDisconnected() {

    }

    /**
     * <p>onReceivedRoomCount</p>
     * <p>当收到弹幕姬发送的人气值时的回调方法</p>
     *
     * @param roomCount 人气值
     * @author 烟雨平生
     */
    @Override
    public void onReceivedRoomCount(Integer roomCount) {

    }

    /**
     * <p>onReceivedComment</p>
     * <p>当收到弹幕消息时的回调方法</p>
     *
     * @param uid         发送人uid
     * @param userName    发送人用户名
     * @param face        用户头像
     * @param commentText 消息内容
     * @param rawData     原始数据
     * @author 烟雨平生
     */
    @Override
    public void onReceivedComment(Long uid, String userName, String face, String commentText, JSONObject rawData) {

    }

    /**
     * <p>onReceivedGift</p>
     * <p>当收到礼物时的回调方法</p>
     *
     * @param uid       发送人uid
     * @param userName  发送人用户名
     * @param giftName  礼物名称
     * @param giftNum 礼物数量
     * @param rawData   原始数据
     * @author 烟雨平生
     */
    @Override
    public void onReceivedGift(Long uid, String userName, String face, Long giftId, String giftName, Long giftNum, Long price, Boolean paid, JSONObject rawData) {

    }

    /**
     * <p>onReceivedGiftTop</p>
     * <p>当收到礼物排行榜时的回调方法</p>
     *
     * @param giftTop 礼物排行榜
     * @author 烟雨平生
     */
    @Override
    public void onReceivedGiftTop(JSONArray giftTop) {

    }

    /**
     * <p>onReceivedGuardBuy</p>
     * <p>当收到弹幕姬开通大航海时的回调方法</p>
     *
     * @param uid        发送人uid
     * @param userName   发送人用户名
     * @param guardLevel 大航海等级 1总督 2提督 3舰长
     * @param guardNum   大航海数量
     * @param guardUnit  大航海单位
     * @param rawData    原始数据
     * @author 烟雨平生
     */
    @Override
    public void onReceivedGuardBuy(Long uid, String userName, Long guardLevel, Long guardNum, String guardUnit, JSONObject rawData) {

    }

    /**
     * <p>onLiveEnd</p>
     * <p>当直播结束时的回调方法</p>
     *
     * @author 烟雨平生
     */
    @Override
    public void onLiveEnd() {

    }

    /**
     * <p>onLiveStart</p>
     * <p>当直播开始时的回调方法</p>
     *
     * @author 烟雨平生
     */
    @Override
    public void onLiveStart() {

    }

    /**
     * <p>onReceivedSuperChat</p>
     * <p>当收到弹幕姬醒目留言时的回调方法</p>
     *
     * @param uid         发送人uid
     * @param userName    发送人用户名
     * @param commentText 消息内容
     * @param price       留言价格
     * @param rawData     原始数据
     */
    @Override
    public void onReceivedSuperChat(Long uid, String userName, String commentText, Long price, JSONObject rawData) {

    }

    /**
     * <p>onReceivedWarning</p>
     * <p>当直播间收到超管警告时的回调方法</p>
     */
    @Override
    public void onReceivedWarning() {

    }

    /**
     * <p>onWelcome</p>
     * <p>当用户进入直播间时的回调方法</p>
     *
     * @param uid      用户uid
     * @param userName 用户名
     * @param rawData  原始数据
     */
    @Override
    public void onWelcome(Long uid, String userName, JSONObject rawData) {

    }

    /**
     * <p>onWelcomeGuard</p>
     * <p>当船员进入直播间时的回调方法</p>
     *
     * @param uid            用户uid
     * @param userName       用户名
     * @param userGuardLevel 船员等级
     *                       <p>1总督 2提督 3舰长</p>
     * @param rawData        原始数据
     */
    @Override
    public void onWelcomeGuard(Long uid, String userName, Integer userGuardLevel, JSONObject rawData) {

    }

    /**
     * <p>onReceivedOnlineRankCount</p>
     * <p>当收到在线排行榜人数时的回调方法</p>
     *
     * @param online 在线排行榜人数
     */
    @Override
    public void onReceivedOnlineRankCount(Integer online) {

    }

    /**
     * <p>onReceivedOnlineRankGold</p>
     * <p>当收到在线高能榜时的回调方法</p>
     *
     * @param rankList 排行榜列表
     */
    @Override
    public void onReceivedOnlineRankGold(List<BiliUser> rankList) {

    }

    /**
     * <p>onHotRankChangedV2</p>
     * <p>当热门榜分区榜变动时的回调方法</p>
     *
     * @param areaName 排行榜名称
     * @param trend    变动趋势 作用暂时不明
     * @param rank     变动名次
     */
    @Override
    public void onHotRankChangedV2(String areaName, Integer trend, Integer rank) {

    }

    /**
     * <p>onHotRankChanged</p>
     * <p>当热门榜总榜变动时的回调方法</p>
     *
     * @param areaName 排行榜名称
     * @param trend    变动趋势 作用暂时不明
     * @param rank     变动名次
     */
    @Override
    public void onHotRankChanged(String areaName, Integer trend, Integer rank) {

    }

    /**
     * <p>onHotRankSettlementV2</p>
     * <p>当热门榜分区榜结算时的回调方法</p>
     *
     * @param areaName 排行榜名称
     * @param rank     排行
     */
    @Override
    public void onHotRankSettlementV2(String areaName, Integer rank) {

    }

    /**
     * <p>onHotRankSettlement</p>
     * <p>当热门榜总榜结算时的回调方法</p>
     *
     * @param areaName 排行榜名称
     * @param rank     排行
     */
    @Override
    public void onHotRankSettlement(String areaName, Integer rank) {

    }

    /**
     * <p>onLiveInteractiveGame</p>
     * <p>直播间互动游戏 通常在GiftSend事件前触发，附带的数据似乎是赠送礼物信息 不要和GiftSend同时使用，以免引起重复计数</p>
     *
     * @param uid      用户id
     * @param userName 用户昵称
     * @param face     用户头像
     * @param giftId   礼物id
     * @param giftName 礼物名称
     * @param giftNum  礼物数量
     * @param price    似乎是金瓜子的价格
     */
    @Override
    public void onLiveInteractiveGame(Long uid, String userName, String face, Integer giftId, String giftName, Integer giftNum, Integer price) {

    }

    /**
     * <p>onComboSend</p>
     * <p>直播间连击礼物 通常在连续多次送礼后触发</p>
     *
     * @param uid      用户id
     * @param userName 用户昵称
     * @param action   用户动作
     * @param giftId   礼物id
     * @param giftName 礼物名称
     * @param comboNum 连击次数
     */
    @Override
    public void onComboSend(Long uid, String userName, String action, Integer giftId, String giftName, Integer comboNum) {

    }
}
