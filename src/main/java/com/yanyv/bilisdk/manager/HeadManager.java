package com.yanyv.bilisdk.manager;

import com.yanyv.bilisdk.BiliLive;
import com.yanyv.bilisdk.vo.BiliUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class HeadManager {
    // 先判断是否有该用户的数据
    public static boolean hasHead(BiliLive biliLive, Long uid, String url) {
        // 先判断是否有该用户的数据
        if (biliLive.getUserMap().containsKey(uid) || UserDataManager.isUserExist(uid)) {
            // 如果存在该用户信息
            // 判断本地是否有该头像
            File file = new File(ConfigManager.getInstance().getDownLoadPath() + uid + ".jpg");
            // 如果有且相同，则返回true
            // 如果没有说明本地没有该头像，或者本地有该头像但是不同，则返回false
            return file.exists() && (url.equals(biliLive.getUserMap().get(uid).getFace()) || url.equals(UserDataManager.getUser(uid).getFace()));
        } else {
            // 如果不存在该用户信息
            return false;
        }
    }

    // 获取头像路径
    public static String getPath(Long uid) {
        return ConfigManager.getInstance().getDownLoadPath() + uid + ".jpg";
    }

    // 从网络下载头像
    public static void downloadHead(BiliLive biliLive, Long uid, String url) {
        // 下载头像
        if (!hasHead(biliLive, uid, url)) {
            // 创建线程
            Thread thread = new Thread(() -> {
                try {
                    // 将字符串转换为URL
                    URL url1 = new URL(url);
                    // 创建输入流
                    InputStream inputStream = url1.openStream();
                    // 创建缓冲区
                    byte[] bytes = new byte[1024];
                    // 创建输出流
                    File file = new File(ConfigManager.getInstance().getDownLoadPath());
                    if (!file.exists()) {
                        boolean wasSuccess = file.mkdirs();
                    }
                    OutputStream outputStream = new FileOutputStream(getPath(uid));
                    // 循环读取输入流
                    int len;
                    while ((len = inputStream.read(bytes)) != -1) {
                        // 将读取的内容写入输出流
                        outputStream.write(bytes, 0, len);
                    }
                    // 关闭输入流
                    inputStream.close();
                    // 关闭输出流
                    outputStream.close();
                    BiliUser biliUser = biliLive.getUserMap().get(uid);
                    biliUser.setDownloaded(true);
                    UserDataManager.setUserData(biliUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            // 启动线程
            thread.start();
        }
    }

    // 获取本地头像位置
    public static String getHeadLocalPath(Long uid) {
        return ConfigManager.getInstance().getDownLoadPath() + uid + ".jpg";
    }
}
