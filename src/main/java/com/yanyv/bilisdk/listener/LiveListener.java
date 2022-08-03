package com.yanyv.bilisdk.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yanyv.bilisdk.vo.BiliUser;

import java.util.List;

/**
 * <h4>LiveListener</h4>
 * <p>需要第三方开发者实现的接口类，用于接收各种事件</p>
 * @author 烟雨平生
 */
public interface LiveListener {

	/**
	 * <h4>onRoomConnected</h4>
	 * <p>当弹幕姬与房间连接成功时的回调方法</p>
	 * @author 烟雨平生
	 */
	void onRoomConnected();

	/**
	 * <h4>onRoomDisconnected</h4>
	 * <p>当弹幕姬与房间断开连接时的回调方法</p>
	 * @author 烟雨平生
	 */
	void onRoomDisconnected();

	/**
	 * <h4>onReceivedRoomCount</h4>
	 * <p>当收到弹幕姬发送的人气值时的回调方法</p>
	 * @param roomCount 人气值
	 * @author 烟雨平生
	 * @deprecated 官方接口未实现
	 */
	@Deprecated
	void onReceivedRoomCount(Integer roomCount);

	/** 
	 * <h4>onReceivedComment</h4>
	 * <p>当收到弹幕消息时的回调方法</p>
	 * @param uid 发送人uid
	 * @param userName 发送人用户名
	 * @param commentText 消息内容
	 * @param rawData 原始数据
	 * @author 烟雨平生
	 */
	void onReceivedComment(Long uid, String userName, String face, String commentText, JSONObject rawData);

	/**
	 * <h4>onReceivedGift</h4>
	 * <p>当收到礼物时的回调方法</p>
	 * @param uid 发送人uid
	 * @param userName 发送人用户名
	 * @param face 用户头像
	 * @param giftId 礼物id
	 * @param giftName 礼物名称
	 * @param giftNum 礼物数量
	 * @param price 礼物价格
	 * @param paid 是否付费
	 * @param rawData 原始数据
	 * @author 烟雨平生
	 */
	void onReceivedGift(Long uid, String userName, String face, Long giftId, String giftName, Long giftNum, Long price, Boolean paid, JSONObject rawData);
	/**
	 * <h4>onReceivedGiftTop</h4>
	 * <p>当收到礼物排行榜时的回调方法</p>
	 * @param giftTop 礼物排行榜
	 * @author 烟雨平生
	 * @deprecated 官方接口未实现
	 */
	@Deprecated
	void onReceivedGiftTop(JSONArray giftTop);

	/**
	 * <h4>onReceivedGuardBuy</h4>
	 * <p>当收到弹幕姬开通大航海时的回调方法</p>
	 * @param uid        发送人uid
	 * @param userName   发送人用户名
	 * @param guardLevel 大航海等级 1总督 2提督 3舰长
	 * @param guardNum   大航海数量
	 * @param guardUnit  大航海单位
	 * @param rawData    原始数据
	 * @author 烟雨平生
	 */
	void onReceivedGuardBuy(Long uid, String userName, Long guardLevel, Long guardNum, String guardUnit, JSONObject rawData);

	/**
	 * <h4>onLiveEnd</h4>
	 * <p>当直播结束时的回调方法</p>
	 * @author 烟雨平生
	 * @deprecated 官方接口未实现
	 */
	@Deprecated
	void onLiveEnd();

	/**
	 * <h4>onLiveStart</h4>
	 * <p>当直播开始时的回调方法</p>
	 * @author 烟雨平生
	 * @deprecated 官方接口未实现
	 */
	@Deprecated
	void onLiveStart();

	/**
	 * <h4>onReceivedSuperChat</h4>
	 * <p>当收到弹幕姬醒目留言时的回调方法</p>
	 * @param uid 发送人uid
	 * @param userName 发送人用户名
	 * @param commentText 消息内容
	 * @param price 留言价格
	 * @param rawData 原始数据
	 */
	void onReceivedSuperChat(Long uid, String userName, String commentText, Long price, JSONObject rawData);

	/**
	 * <h4>onReceivedWarning</h4>
	 * <p>当直播间收到超管警告时的回调方法</p>
	 * @deprecated 官方接口未实现
	 */
	@Deprecated
	void onReceivedWarning();

	/**
	 * <h4>onWelcome</h4>
	 * <p>当用户进入直播间时的回调方法</p>
	 * @param uid 用户uid
	 * @param userName 用户名
	 * @param rawData 原始数据
	 * @deprecated 官方接口未实现
	 */
	@Deprecated
	void onWelcome(Long uid, String userName, JSONObject rawData);

	/**
	 * <h4>onWelcomeGuard</h4>
	 * <p>当船员进入直播间时的回调方法</p>
	 * @param uid 用户uid
	 * @param userName 用户名
	 * @param userGuardLevel 船员等级
	 *                       <div>
	 *                       	<table>
	 * 								<tr>
	 * 									<th>值&nbsp</th>
	 * 									<th>含义</th>
	 * 								</tr>
	 * 								<tr>
	 * 									<td>0</td>
	 * 									<td>非船员</td>
	 * 								</tr>
	 * 								<tr>
	 * 									<td>1</td>
	 * 									<td>总督</td>
	 * 								</tr>
	 * 								<tr>
	 * 									<td>2</td>
	 * 									<td>提督</td>
	 * 								</tr>
	 * 								<tr>
	 * 									<td>3</td>
	 * 									<td>舰长</td>
	 * 								</tr>
	 * 							</table>
	 *                       </div>
	 * @param rawData 原始数据
	 * @deprecated 官方接口未实现
	 */
	@Deprecated
	void onWelcomeGuard(Long uid, String userName, Integer userGuardLevel, JSONObject rawData);

	// 以下方法通过未知消息处理器调用，已经过二次处理，不提供原始数据
	/**
	 * <h4>onReceivedOnlineRankCount</h4>
	 * <p>当收到在线排行榜人数时的回调方法</p>
	 * @param online 在线排行榜人数
	 * @deprecated 官方接口未实现
	 */
	@Deprecated
	void onReceivedOnlineRankCount(Integer online);

	/**
	 * <h4>onReceivedOnlineRankGold</h4>
	 * <p>当收到在线高能榜时的回调方法</p>
	 * @param rankList 排行榜列表
	 * @deprecated 官方接口未实现
	 */
	@Deprecated
	void onReceivedOnlineRankGold(List<BiliUser> rankList);

	/**
	 * <h4>onHotRankChangedV2</h4>
	 * <p>当热门榜分区榜变动时的回调方法</p>
	 * @param areaName 排行榜名称
	 * @param trend 变动趋势 作用暂时不明
	 * @param rank 变动名次
	 * @deprecated 官方接口未实现
	 */
	@Deprecated
	void onHotRankChangedV2(String areaName, Integer trend, Integer rank);

	/**
	 * <h4>onHotRankChanged</h4>
	 * <p>当热门榜总榜变动时的回调方法</p>
	 * @param areaName 排行榜名称
	 * @param trend 变动趋势 作用暂时不明
	 * @param rank 变动名次
	 * @deprecated 官方接口未实现
	 */
	@Deprecated
	void onHotRankChanged(String areaName, Integer trend, Integer rank);

	/**
	 * <h4>onHotRankSettlementV2</h4>
	 * <p>当热门榜分区榜结算时的回调方法</p>
	 * @param areaName 排行榜名称
	 * @param rank 排行
	 * @deprecated 官方接口未实现
	 */
	@Deprecated
	void onHotRankSettlementV2(String areaName, Integer rank);

	/**
	 * <h4>onHotRankSettlement</h4>
	 * <p>当热门榜总榜结算时的回调方法</p>
	 * @param areaName 排行榜名称
	 * @param rank 排行
	 * @deprecated 官方接口未实现
	 */
	@Deprecated
	void onHotRankSettlement(String areaName, Integer rank);

	/**
	 * <h4>onLiveInteractiveGame</h4>
	 * <p>直播间互动游戏 通常在GiftSend事件前触发，附带的数据似乎是赠送礼物信息 不要和GiftSend同时使用，以免引起重复计数</p>
	 * @param uid 用户id
	 * @param userName 用户昵称
	 * @param face 用户头像
	 * @param giftId 礼物id
	 * @param giftName 礼物名称
	 * @param giftNum 礼物数量
	 * @param price 似乎是金瓜子的价格
	 * @deprecated 官方接口已弃用
	 */
	@Deprecated
	void onLiveInteractiveGame(Long uid, String userName, String face, Integer giftId, String giftName, Integer giftNum, Integer price);

	/**
	 * <h4>onComboSend</h4>
	 * <p>直播间连击礼物 通常在连续多次送礼后触发</p>
	 * @param uid 用户id
	 * @param userName 用户昵称
	 * @param action 用户动作
	 * @param giftId 礼物id
	 * @param giftName 礼物名称
	 * @param comboNum 连击次数
	 * @deprecated 官方接口未实现
	 */
	@Deprecated
	void onComboSend(Long uid, String userName, String action, Integer giftId, String giftName, Integer comboNum);

}
