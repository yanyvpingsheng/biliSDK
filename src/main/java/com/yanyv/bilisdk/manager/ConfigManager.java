package com.yanyv.bilisdk.manager;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ConfigManager {

    private static ConfigManager instance = null;
    private static final String configPath = "data/config.json";
    private final JSONObject config;

    /**
     * 默认配置
     */
    private ConfigManager() {
        config = new JSONObject();
        // 头像下载路径
        setDownLoadPath("data/head/");
        setUsersData(new JSONObject());
    }

    /**
     * 通过文件加载配置
     */
    public ConfigManager(JSONObject config) {
        this.config = config;
    }

    public String getDownLoadPath() {
        return config.getString("downloadPath");
    }

    public void setDownLoadPath(String path) {
        config.put("downloadPath", path);
    }

    /**
     * 设置用户数据
     */
    private void setUsersData(JSONObject data) {
        config.put("usersData", data);
    }

    /**
     * 获取用户数据
     * @return 用户数据 以用户uid为键值的JSONObject对象
     */
    public JSONObject getUsersData() {
        return config.getJSONObject("usersData");
    }

    /**
     * 创建默认配置
     */
    private static ConfigManager createConfig() {
        ConfigManager config = new ConfigManager();
        saveConfig(config);
        return config;
    }

    /**
     * 保存配置
     * @param configManager 配置
     */
    public static void saveConfig(ConfigManager configManager) {
        File file = new File(configPath);
        FileWriter fileWriter;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file.getPath(), true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            BufferedWriter out = new BufferedWriter(outputStreamWriter);

            fileWriter = new FileWriter(file.getPath());
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();

            out.write(configManager.config.toString());
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载配置
     * @return 配置
     */
    private static ConfigManager loadConfig() {
        File file = new File(configPath);
        // 如果配置文件不存在，则创建默认配置
        if (!file.exists()) {
            try {
                // 尝试创建配置文件，如果成功则返回默认配置
                if(file.createNewFile()) {
                    return createConfig();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // 配置文件存在，则加载配置
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(configPath);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(inputStreamReader);
            String str;
            if ((str = in.readLine()) != null) {
                in.close();
                inputStreamReader.close();
                fileInputStream.close();
                return new ConfigManager(JSONObject.parseObject(str));
            }
            in.close();
            inputStreamReader.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ConfigManager();
    }

    // 懒汉式单例获取配置
    public static ConfigManager getInstance() {
        // 如果配置文件尚未加载，则加载配置文件
        if(instance == null) {
            instance = loadConfig();
        }
        return instance;
    }
}
