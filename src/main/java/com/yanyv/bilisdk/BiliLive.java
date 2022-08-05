package com.yanyv.bilisdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.yanyv.bilisdk.listener.LiveListener;
import com.yanyv.bilisdk.listener.SystemLiveListener;
import com.yanyv.bilisdk.util.BiliUtil;
import com.yanyv.bilisdk.vo.BiliUser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>BiliLive</p>
 * <p>用于控制直播间的操作</p>
 * @author 烟雨平生
 * @version V0.1.0
 */
public class BiliLive {

	// 单例模式
	private static BiliLive biliLive = null;

	// 暴露给第三方开发的接口
	private final List<LiveListener> listeners = new ArrayList<>();
	private final Map<Long, BiliUser> userMap = new HashMap<>();

	// 哔哩哔哩配置
	private BiliConfig biliConfig;

	// 连接状态
	private boolean connecting = false;

	// 场次id
	private String gameId = "";
	// 通关文牒
	private String authBody = "";
	// wss连接
	private WebSocket ws = null;

	/**
	 * <p>Method ：BiliLive
	 * <p>Description : 构造方法 当构造方法被调用时，会自动添加系统监听器，用于实现基本功能
	 *
	 * @param config 哔哩哔哩配置
	 * @author 烟雨平生 yanyvpingsheng@qq.com
	 *<p>
	 *--------------------------------------------------------------<br>
	 * 修改履历：<br>
	 *        <li> 2022/8/3，yanyvpingsheng@qq.com，创建方法；<br>
	 *--------------------------------------------------------------<br>
	 *</p>
	 */
	private BiliLive(BiliConfig config) {
		this.biliConfig = config;
		this.addListener(new SystemLiveListener(this));
	}

	/**
	 * <p>Method ：addListener
	 * <p>Description : 添加监听器
	 *
	 * @param listener 监听器
	 * @author 烟雨平生 yanyvpingsheng@qq.com
	 */
	public void addListener(LiveListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * <p>Method ：connect
	 * <p>Description : 连接直播间
	 *
	 * @author 烟雨平生 yanyvpingsheng@qq.com
	 */
	public void connect() {
		BiliUtil.init(biliConfig);
		JSONObject input = new JSONObject();
		input.put("code", biliConfig.getCode());
		input.put("app_id", biliConfig.getAppid());
		System.out.println("[BiliLive]正在尝试连接到哔哩哔哩世界..." + input);
		JSONObject result = BiliUtil.post("v2/app/start", input);
		if(result.isEmpty()) {
			System.out.println("[BiliLive]连接失败..." + result);
			return;
		}
		System.out.println("[BiliLive]已成功连接到哔哩哔哩世界..." + result);
		this.gameId = result.getJSONObject("data").getJSONObject("game_info").getString("game_id");
		this.authBody = result.getJSONObject("data").getJSONObject("websocket_info").getString("auth_body");
		this.connecting = true;
		// 建立wss连接
		WebSocketFactory factory = new WebSocketFactory();
		try {
			ws = factory.createSocket(result.getJSONObject("data").getJSONObject("websocket_info").getJSONArray("wss_link").getString(0));
			ws.addListener(new WebSocketAdapter() {

				@Override
				public void onBinaryMessage(WebSocket websocket, byte[] binary) {
					// System.out.println(Arrays.toString(binary));
					switch (binary[11]) {
						// 心跳回复
						case 3:
							System.out.println("[BiliLive-wss]弹幕娘收到了你的小心心并踢了你一jio");
							break;
						// 弹幕消息
						case 5:
							// 截取弹幕消息
							byte[] data = new byte[binary.length - 16];
							System.arraycopy(binary, 16, data, 0, data.length);
							// 解析弹幕消息
							BiliLive.this.receiveMessage(JSON.parseObject(new String(data, StandardCharsets.UTF_8)));
							break;
						// 握手回复
						case 8:
							System.out.println("[BiliLive-wss]已与弹幕娘友好的握手...");
							for(LiveListener listener: listeners) {
								listener.onRoomConnected();
							}
							break;
					}
				}

				@Override
				public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
					// Connected to the server. Send a message.
					System.out.println("[BiliLive-wss]已与弹幕娘在哔哩哔哩世界会晤...");
					// 将授权码以UTF-8格式转为二进制
					byte[] body = BiliLive.this.authBody.getBytes(StandardCharsets.UTF_8);
					// 编码后的内容长度加上固定16的包头，推算出数据包总长度
					int pkgLength = body.length + 16;
					// 包头
					byte[] pkgData = new byte[pkgLength];
					// 【Packet Length】包长字节数(Byte)【每隔字节8个位(Bit)，即数组里的一个UInt8】
					// 4字节[Byte]长度的包总长度，后两个为实际长度的高位和低位
					// 这里用了取巧的写法，毕竟字节码是一个Byte是0到255，所以用255直接进位
					// 至于前两位……你跟我说你能发个长度超过65535的包？
					pkgData[0] = 0;
					pkgData[1] = 0;
					pkgData[2] = (byte) (pkgLength / 255);
					pkgData[3] = (byte) (pkgLength % 255);
					// 【Header Length】数据包包头长度，固定16，写死写死！
					pkgData[4] = 0;
					pkgData[5] = 16;
					// 【Version】协议版本1，0表示无加密，2是zlib压缩过的，开平长链里没有压缩过的数据
					pkgData[6] = 0;
					pkgData[7] = 0;
					// 操作码，这里写死，7为OP_AUTH，登录握手包
					pkgData[8] = 0;
					pkgData[9] = 0;
					pkgData[10] = 0;
					pkgData[11] = 7;
					// 不知道干啥的“Sequence ID”，瞎写
					// 【但是之前有很多野生API这里会写1？】
					pkgData[12] = 0;
					pkgData[13] = 0;
					pkgData[14] = 0;
					pkgData[15] = 0;
					System.arraycopy(body, 0, pkgData, 16, body.length);

					System.out.println("[BiliLive-wss]准备与弹幕娘进行友好的握手...");
					websocket.sendBinary(pkgData);
				}
			});
			ws.connect();
		} catch (IOException | WebSocketException e) {
			System.out.println("[BiliLive]连接失败..." + result.getJSONObject("data").getJSONObject("websocket_info").getJSONArray("wss_link").getString(0));
			e.printStackTrace();
			return;
		}
		// 每隔20秒发送心跳
		new Thread(() -> {
			while (connecting) {
				try {
					Thread.sleep(20 * 1000);
					if(!connecting) break;
					// http心跳
					JSONObject input2 = new JSONObject();
					input2.put("game_id", BiliLive.this.gameId);
					System.out.println("[BiliLive]正在向哔哩哔哩世界发送例行通告..." + input2);
					JSONObject result2 = BiliUtil.post("v2/app/heartbeat", input2);
					System.out.println("[BiliLive]通告结果..." + result2);
					// wss心跳
					byte[] pkgData = new byte[]{
						// 【Packet Length】包长16字节(Byte)【16个8位(Bit)】
						0, 0, 0, 16,
						// 【Header Length】数据包包头长度，固定16，写死写死！
						0, 16,
						// 【Version】协议版本1，0表示无加密，2是zlib压缩过的，开平长链里没有压缩过的数据
						// 【但是之前有很多野生API这里会写1？】
						0, 0,
						// 【Operation】操作类型，这里写死，2为OP_HEARTBEAT，客户端心跳，KeepAlive包
						0, 0, 0, 2,
						// 不知道干啥的“Sequence ID”，瞎写
						0, 0, 0, 0
					};

					System.out.println("[BiliLive-wss]正在向弹幕娘发射小心心...");
					ws.sendBinary(pkgData);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**
	 * <p>Method ：disconnect
	 * <p>Description : 断开连接
	 *
	 * @author 烟雨平生 yanyvpingsheng@qq.com
	 */
	public void disconnect() {
		// 主动断开连接
		this.connecting = false;
		JSONObject input = new JSONObject();
		input.put("game_id", this.gameId);
		input.put("app_id", biliConfig.getAppid());
		System.out.println("[BiliLive]正在断开与哔哩哔哩世界的连接..." + input);
		JSONObject result = BiliUtil.post("v2/app/end", input);

		System.out.println("[BiliLive-wss]正在与弹幕娘挥手...");
		ws.disconnect();
		for(LiveListener listener: listeners) {
			listener.onRoomDisconnected();
		}
		System.out.println("[BiliLive]已成功断开连接..." + result);
	}

	/**
	 * <p>receiveMessage</p>
	 * <p>处理弹幕消息</p>
	 * @param obj 弹幕消息数据
	 * @author 烟雨平生
	 */
	private void receiveMessage(JSONObject obj) {
		// TODO Auto-generated method stub
		String cmd = obj.getString("cmd");
		obj = obj.getJSONObject("data");
		switch (cmd) {
		// 弹幕消息
		case "LIVE_OPEN_PLATFORM_DM":
			//System.out.println(obj.getString("UserName") + "说：" + obj.getString("CommentText"));
			for (LiveListener listener : listeners) {
				listener.onReceivedComment(
						obj.getLong("uid"),
						obj.getString("uname"),
						obj.getString("uface"),
						obj.getString("msg"),
						obj);
			}
			break;
		// 礼物消息
		case "LIVE_OPEN_PLATFORM_SEND_GIFT":
			for (LiveListener listener : listeners) {
				listener.onReceivedGift(
						obj.getLong("uid"),
						obj.getString("uname"),
						obj.getString("uface"),
						obj.getLong("gift_id"),
						obj.getString("gift_name"),
						obj.getLong("gift_num"),
						obj.getLong("price"),
						obj.getBoolean("paid"),
						obj
				);
			}
			break;
//		case GiftTop:
//			for (LiveListener listener : listeners) {
//				listener.onReceivedGiftTop(obj.getJSONArray("GiftRanking"));
//			}
//			break;
		// 开通大航海
		case "LIVE_OPEN_PLATFORM_GUARD":
			for (LiveListener listener : listeners) {
				listener.onReceivedGuardBuy(
						obj.getJSONObject("user_info").getLong("uid"),
						obj.getJSONObject("user_info").getString("uname"),
						obj.getLong("guard_level"),
						obj.getLong("guard_num"),
						obj.getString("guard_unit"),
						obj
				);
			}
			break;
//		case Interact:
//			for (LiveListener listener : listeners) {
//				listener.onReceivedInteract(
//						obj.getInteger("UserID"),
//						obj.getString("UserName"),
//						obj.getInteger("InteractType"),
//						rawData
//				);
//			}
//			break;
//		case LiveEnd:
//			for (LiveListener listener : listeners) {
//				listener.onLiveEnd();
//			}
//			break;
//		case LiveStart:
//			for (LiveListener listener : listeners) {
//				listener.onLiveStart();
//			}
//			break;
		// 付费留言
		case "LIVE_OPEN_PLATFORM_SUPER_CHAT":
			for (LiveListener listener : listeners) {
				listener.onReceivedSuperChat(
						obj.getLong("uid"),
						obj.getString("uname"),
						obj.getString("message"),
						obj.getLong("rmb"),
						obj
				);
			}
			break;
//		case Warning:
//			for (LiveListener listener : listeners) {
//				listener.onReceivedWarning();
//			}
//			break;
//		case Welcome:
//			for (LiveListener listener : listeners) {
//				listener.onWelcome(
//						obj.getInteger("UserID"),
//						obj.getString("UserName"),
//						rawData
//				);
//			}
//			break;
//		case WelcomeGuard:
//			for (LiveListener listener : listeners) {
//				listener.onWelcomeGuard(
//						obj.getInteger("UserID"),
//						obj.getString("UserName"),
//						obj.getInteger("UserGuardLevel"),
//						rawData
//				);
//			}
//			break;
//		case Unknown:
//			// 调用未知消息处理类
//			UnknownCommentController.dispatch(rawData, listeners);
//		case WatchedChange:
		default:
			System.out.println("[BiliLive-wss]未知消息" + obj);
			break;
		}
	}

	public Map<Long, BiliUser> getUserMap() {
		return userMap;
	}

	/**
	 * 懒汉式单例
	 * @param config 哔哩哔哩配置
	 * @return 实例
	 */
	public static BiliLive getInstance(BiliConfig config) {
		if(biliLive == null) {
			biliLive = new BiliLive(config);
		}
		return biliLive;
	}
}
