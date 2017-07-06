package cn.nj.ljy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

public class ParseMD5 {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParseMD5.class);

    /**
     * 
     * 功能描述: <br>
     * 32位小写MD5
     *
     * @version [V1.0, 2017年5月19日]
     * @param str 加密字符串
     * @return MD5 字符串
     */

    public static String parseStrToMd5L32(String str) {

        if (null == str) {
            LOGGER.warn(".parseStrToMd5L32() str is null.");
            return str;
        }

        String reStr = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                int bt = b & 0xff;
                if (bt < 16) {
                    sb.append(0);
                }
                sb.append(Integer.toHexString(bt));
            }

            reStr = sb.toString();

            return reStr;

        } catch (Exception e) {
            LOGGER.error(".parseStrToMd5L32() Exception={},param={}", e, str, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 
     * 功能描述: <br>
     * 2位大写MD5
     *
     * @version [V1.0, 2017年5月19日]
     * @param str 加密字符串
     * @return md5字符串
     */
    public static String parseStrToMd5U32(String str) {
        String reStr = parseStrToMd5L32(str);
        if (null != reStr) {
            reStr = reStr.toUpperCase();
        }
        return reStr;
    }
}
