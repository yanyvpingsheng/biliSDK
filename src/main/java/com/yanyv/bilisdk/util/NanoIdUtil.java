package com.yanyv.bilisdk.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * <p>Class       : com.yanyv.bilisdk.util.NanoIdUtil
 * <p>Descdription: 生成nanoId的工具类
 *
 * @author 烟雨平生 yanyvpingsheng@qq.com
 * @version 1.0.0
 * NanoId工具
 */
public final class NanoIdUtil {
    public static final SecureRandom DEFAULT_NUMBER_GENERATOR = new SecureRandom();
    public static final char[] DEFAULT_ALPHABET = "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    public static final int DEFAULT_SIZE = 21;

    private NanoIdUtil() {
    }

    /**
     * <p>Method ：randomNanoId
     * <p>Description : 使用预设值生成nanoId
     * @return nanoId
     * @author 烟雨平生 yanyvpingsheng@qq.com
     */
    public static String randomNanoId() {
        return randomNanoId(DEFAULT_NUMBER_GENERATOR, DEFAULT_ALPHABET, DEFAULT_SIZE);
    }

    /**
     * <p>Method ：randomNanoId
     * <p>Description : 生成nanoId
     *
     * @param random 随机数生成器
     * @param alphabet 字符集
     * @param size 字符串长度
     * @return nanoId
     * @author 烟雨平生 yanyvpingsheng@qq.com
     */
    public static String randomNanoId(Random random, char[] alphabet, int size) {
        if (random == null) {
            throw new IllegalArgumentException("随机类对象不能为空");
        } else if (alphabet == null) {
            throw new IllegalArgumentException("字符集不能为空");
        } else if (alphabet.length != 0 && alphabet.length < 256) {
            if (size <= 0) {
                throw new IllegalArgumentException("长度必须大于0");
            } else {
                int mask = (2 << (int)Math.floor(Math.log((double)(alphabet.length - 1)) / Math.log(2.0D))) - 1;
                int step = (int)Math.ceil(1.6D * (double)mask * (double)size / (double)alphabet.length);
                StringBuilder idBuilder = new StringBuilder();

                while(true) {
                    byte[] bytes = new byte[step];
                    random.nextBytes(bytes);

                    for(int i = 0; i < step; ++i) {
                        int alphabetIndex = bytes[i] & mask;
                        if (alphabetIndex < alphabet.length) {
                            idBuilder.append(alphabet[alphabetIndex]);
                            if (idBuilder.length() == size) {
                                return idBuilder.toString();
                            }
                        }
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("字符集长度必须大于0且小于256");
        }
    }
}
