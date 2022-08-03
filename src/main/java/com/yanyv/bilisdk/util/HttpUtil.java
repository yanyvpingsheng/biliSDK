package com.yanyv.bilisdk.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * <p>Class       : com.yanyv.bilisdk.util.HttpUtil
 * <p>Descdription: 发送http请求的工具类
 *
 * @author 烟雨平生 yanyvpingsheng@qq.com
 * @version 1.0.0
 * HTTP工具类
 * <p>
 * --------------------------------------------------------------<br>
 * 修改履历：<br>
 * <li> 2022/8/2，yanyvpingsheng@qq.com，创建文件；<br>
 * --------------------------------------------------------------<br>
 * </p>
 */
public final class HttpUtil {

    // 创建连接对象
    private static final CloseableHttpClient client = HttpClients.createDefault();

    /**
     * <p>Method ：doPost
     * <p>Description : 发送post请求
     *
     * @param url url
     * @param headers 请求头
     * @param param 请求体数据，json格式
     * @author 烟雨平生 yanyvpingsheng@qq.com
     *<p>
     *--------------------------------------------------------------<br>
     * 修改履历：<br>
     *        <li> 2022/8/3，yanyvpingsheng@qq.com，创建方法；<br>
     *--------------------------------------------------------------<br>
     *</p>
     */
    public static JSONObject doPost(String url, Header[] headers, JSONObject param) {
        //定义接收数据
        JSONObject result = new JSONObject();
        // 创建post请求对象
        HttpPost httpPost = new HttpPost(url);
        // 请求参数转JSON字符串
        StringEntity entity = new StringEntity(param.toString(), "UTF-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeaders(headers);
        try {
            // 发送请求
            HttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.put("error", "连接错误！");
        }
        return result;
    }

}
