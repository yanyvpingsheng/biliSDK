package com.yanyv.bilisdk;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yanyv.bilisdk.listener.LiveListener;
import com.yanyv.bilisdk.vo.BiliUser;

import java.util.List;

/**
 * <p>UnknownCommentController</p>
 * <p>处理弹幕姬的未知消息</p>
 * @author 烟雨平生
 */
public class UnknownCommentController {
    /**
     *
     * <p>根据未知消息的json对象中的cmd属性分发事件</p>
     * @param json 未知消息的json对象
     * @param listeners 开发者自定义的监听器类，需要实现LiveListener接口
     */
    public static void dispatch(JSONObject json, List<LiveListener> listeners) {
        String cmd = json.getString("cmd");
        JSONObject data = json.getJSONObject("data");
        switch (cmd) {
            case "ONLINE_RANK_COUNT":
                // 在线人数
                Integer online = data.getInteger("count");
                for (LiveListener listener : listeners) {
                    listener.onReceivedOnlineRankCount(online);
                }
                break;
            case "ONLINE_RANK_V2":
                // 在线排行榜
                String rankType = data.getString("rank_type");
                // 判断排行榜类型
                switch (rankType) {
                    case "gold-rank":
                        // 高能榜
                        // 获取高能榜数据
                        JSONArray goldRank = data.getJSONArray("list");
                        // 转为User列表 注意：fastjson是根据构造方法的入参变量名与key进行匹配的
                        List<BiliUser> goldBiliUsers = JSONArray.parseArray(goldRank.toJSONString(), BiliUser.class);
                        for (LiveListener listener : listeners) {
                            listener.onReceivedOnlineRankGold(goldBiliUsers);
                        }
                        break;
                }
                break;
            case "HOT_RANK_CHANGED_V2":
                // 主播实时活动排名 二级排名
                // 获取主播实时活动排名数据
                for (LiveListener listener : listeners) {
                    listener.onHotRankChangedV2(data.getString("area_name"), data.getInteger("trend"), data.getInteger("rank"));
                }
                break;
            case "HOT_RANK_CHANGED":
                // 主播实时活动排名
                // 获取主播实时活动排名数据
                for (LiveListener listener : listeners) {
                    listener.onHotRankChanged(data.getString("area_name"), data.getInteger("trend"), data.getInteger("rank"));
                }
                break;
            case "HOT_RANK_SETTLEMENT_V2":
                // 主播实时活动排名结算 二级排名
                for (LiveListener listener : listeners) {
                    listener.onHotRankSettlementV2(data.getString("area_name"), data.getInteger("rank"));
                }
                break;
            case "HOT_RANK_SETTLEMENT":
                // 主播实时活动排名结算
                for (LiveListener listener : listeners) {
                    listener.onHotRankSettlement(data.getString("area_name"), data.getInteger("rank"));
                }
                break;
            case "LIVE_INTERACTIVE_GAME":
                // 直播间互动游戏 通常在GiftSend事件前触发，附带的数据似乎是赠送礼物信息 不要和GiftSend同时使用，以免引起重复计数
                for (LiveListener listener : listeners) {
                    listener.onLiveInteractiveGame(
                            data.getLong("uid"),
                            data.getString("uname"),
                            data.getString("uface"),
                            data.getInteger("gift_id"),
                            data.getString("gift_name"),
                            data.getInteger("gift_num"),
                            data.getInteger("price")
                    );
                }
                break;
            case "COMBO_SEND":
                // 直播间连击礼物 通常在连续多次送礼后触发
                for (LiveListener listener : listeners) {
                    listener.onComboSend(
                            data.getLong("uid"),
                            data.getString("uname"),
                            data.getString("action"),
                            data.getInteger("gift_id"),
                            data.getString("gift_name"),
                            data.getInteger("combo_num")
                    );
                }
                break;
            // 未知消息：{"data":{"content_segments":[{"font_color":"#FB7299","text":"巧可桜 送出的红包为主播新增35个粉丝！","type":1}],"dmscore":144,"terminals":[2,3,5]},"cmd":"COMMON_NOTICE_DANMAKU"}
            // 似乎是红包新增粉丝的消息
            case "ONLINE_RANK_TOP3":
                // 在线排行榜前三名更新的通知 暂时不处理
                break;
            case "STOP_LIVE_ROOM_LIST":
                // 似乎是B站停止直播的房间列表，不知道有什么用
                break;
            default:
                System.out.println("未知消息：" + json.toJSONString());
                break;
        }
    }

}
