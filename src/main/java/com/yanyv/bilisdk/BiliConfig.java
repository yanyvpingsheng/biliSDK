package com.yanyv.bilisdk;

/**
 * <p>BiliConfig</p>
 * <p>配置类</p>
 * @author 烟雨平生
 */
public class BiliConfig {

    private String accessKeyId;
    private String accessKeySecret;
    private long appid;
    private String code;

    // 哔哩哔哩配置 密钥、密匙、appid、身份码
    /**
     * <p>Method ：BiliConfig
     * <p>Description : 哔哩哔哩配置 密钥、密匙、appid、身份码
     *
     * @param accessKeyId     密钥
     * @param accessKeySecret 密匙
     * @param appid           appid
     * @param code            身份码
     * @author 烟雨平生 yanyvpingsheng@qq.com
     */
    public BiliConfig(String accessKeyId, String accessKeySecret, long appid, String code) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.appid = appid;
        this.code = code;
    }

    public BiliConfig(){}

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public long getAppid() {
        return appid;
    }

    public void setAppid(long appid) {
        this.appid = appid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
