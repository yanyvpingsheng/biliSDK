package com.yanyv.bilisdk.util;

import com.alibaba.fastjson.JSONObject;
import com.yanyv.bilisdk.BiliConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Class       : com.yanyv.bilisdk.util.BiliUtil
 * <p>Descdription: 连接哔哩哔哩直播间的工具类
 *
 * @author 烟雨平生 yanyvpingsheng@qq.com
 * @version 1.0.0
 * 建立与哔哩哔哩世界连接的工具类
 * <p>
 * --------------------------------------------------------------<br>
 * 修改履历：<br>
 * <li> 2022/8/2，yanyvpingsheng@qq.com，创建文件；<br>
 * --------------------------------------------------------------<br>
 * </p>
 */
public final class BiliUtil {
    // 哔哩哔哩世界的域名
    private static final String biliUrl = "https://live-open.biliapi.com/";
    // 待签名请求头
    private static final String waitSignTemplate = "x-bili-accesskeyid:${accessKeyId}\n" +
            "x-bili-content-md5:${contentMd5}\n" +
            "x-bili-signature-method:HMAC-SHA256\n" +
            "x-bili-signature-nonce:${signatureNonce}\n" +
            "x-bili-signature-version:1.0\n" +
            "x-bili-timestamp:${timestamp}";
    // 接受的返回结果类型
    private static final String accept = "Accept:application/json\n";
    // 请求体数据类型
    private static final String contentType = "Content-Type:application/json\n";
    // 请求签名
    private static final String authorization = "Authorization:${authorization}";

    private static String accessKeyId;
    private static String accessKeySecret;

    /***
     * <p>Method ：post
     * <p>Description : 发送post请求，所有向哔哩哔哩世界发送的请求都通过此方法请求
     *
     * @param uri 接口路径，没有前斜杠
     * @param data 请求体数据，json格式
     * @return 返回哔哩哔哩世界的响应结果
     * @author 烟雨平生 yanyvpingsheng@qq.com
     *<p>
     *--------------------------------------------------------------<br>
     * 修改履历：<br>
     *        <li> 2022/8/3，yanyvpingsheng@qq.com，创建方法；<br>
     *--------------------------------------------------------------<br>
     *</p>
     */
    public static JSONObject post(String uri, JSONObject data) {
        // 创建请求头
        String header = getBiliHeader(data.toString());
        String[] headerList = header.split("\n");
        String[][] headerArray = new String[headerList.length][2];
        for (int i = 0; i < headerList.length; i++) {
            String[] headerItem = headerList[i].split(":");
            headerArray[i][0] = headerItem[0];
            headerArray[i][1] = headerItem[1];
        }
        List<Header> headers = new ArrayList<>();
        for (String[] strings : headerArray) {
            headers.add(new BasicHeader(strings[0], strings[1]));
        }
        return HttpUtil.doPost(biliUrl + uri, headers.toArray(new Header[0]), data);
    }

    /**
     * <p>Method ：getBiliWaitSign
     * <p>Description : 获取待签名信息
     *
     * @param content 请求体数据，json格式
     * @author 烟雨平生 yanyvpingsheng@qq.com
     * @return 待签名信息
     *<p>
     *--------------------------------------------------------------<br>
     * 修改履历：<br>
     *        <li> 2022/8/3，yanyvpingsheng@qq.com，创建方法；<br>
     *--------------------------------------------------------------<br>
     *</p>
     */
    public static String getBiliWaitSign(String content) {
        // md5加密请求内容
        String contentMd5 = DigestUtils.md5Hex(content);
        // 随机值
        String signatureNonce = NanoIdUtil.randomNanoId();
        // 时间戳
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        // 拼接待签名信息
        Map<String, Object> prop = new HashMap<>();
        prop.put("accessKeyId", accessKeyId);
        prop.put("contentMd5", contentMd5);
        prop.put("signatureNonce", signatureNonce);
        prop.put("timestamp", timestamp);
        return replaceVariable(waitSignTemplate, prop);
    }

    /***
     * <p>Method ：getBiliHeader
     * <p>Description : 生成请求头
     *
     * @param content 请求体数据，json格式
     * @author 烟雨平生 yanyvpingsheng@qq.com
     * @return 请求头
     *<p>
     *--------------------------------------------------------------<br>
     * 修改履历：<br>
     *        <li> 2022/8/3，yanyvpingsheng@qq.com，创建方法；<br>
     *--------------------------------------------------------------<br>
     *</p>
     */
    public static String getBiliHeader(String content) {
        // 获取待签名信息
        String waitSign = getBiliWaitSign(content);
        // 获取签名
        String signed = sign(waitSign, accessKeySecret);
        // 拼接请求头
        Map<String, Object> prop = new HashMap<>();
        prop.put("authorization", signed);
        StringBuilder sb = new StringBuilder();
        sb.append(accept);
        sb.append(contentType);
        sb.append(waitSign);
        sb.append("\n");
        sb.append(replaceVariable(authorization, prop));
        return sb.toString();
    }

    /**
     * 替换字符串中 ${} 标识的变量
     */
    /**
     * <p>Method ：replaceVariable
     * <p>Description : 替换字符串中 ${} 标识的变量
     *
     * @param template 模板
     * @param properties 变量值
     * @author 烟雨平生 yanyvpingsheng@qq.com
     *<p>
     *--------------------------------------------------------------<br>
     * 修改履历：<br>
     *        <li> 2022/8/3，yanyvpingsheng@qq.com，创建方法；<br>
     *--------------------------------------------------------------<br>
     *</p>
     */
    public static String replaceVariable(String template, Map<String, Object> properties) {
        StringBuffer sb = new StringBuffer();
        // 该表达式匹配${中的数字字母下划线，英文点和$
        Matcher matcher = Pattern.compile("\\$\\{[\\.\\$\\w]+}").matcher(template);
        while (matcher.find()) {
            String param = matcher.group();
            String varible = param.substring(2, param.length() - 1);
            // 追加并替换变量
            Object value = properties.get(varible);
            matcher.appendReplacement(sb, value == null ? "" : String.valueOf(value));
        }
        // 复制输入序列的其余部分
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * <p>Method ：sign
     * <p>Description : 生成 HMACSHA256 签名
     *
     * @param data 待处理数据
     * @param key 密钥
     * @return 加密结果
     * @author 烟雨平生 yanyvpingsheng@qq.com
     *<p>
     *--------------------------------------------------------------<br>
     * 修改履历：<br>
     *        <li> 2022/8/3，yanyvpingsheng@qq.com，创建方法；<br>
     *--------------------------------------------------------------<br>
     *</p>
     */
    public static String sign(String data, String key) {
        Mac sha256_HMAC = null;
        try {
            sha256_HMAC = Mac.getInstance("HmacSHA256");

            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] array = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            System.err.println("无效的密匙"+key);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>Method ：init
     * <p>Description : 初始化，必须调用此方法才能正确发起请求
     *
     * @param config
     * @author 烟雨平生 yanyvpingsheng@qq.com
     *<p>
     *--------------------------------------------------------------<br>
     * 修改履历：<br>
     *        <li> 2022/8/3，yanyvpingsheng@qq.com，创建方法；<br>
     *--------------------------------------------------------------<br>
     *</p>
     */
    public static void init(BiliConfig config) {
        BiliUtil.accessKeyId = config.getAccessKeyId();
        BiliUtil.accessKeySecret = config.getAccessKeySecret();
    }
}
