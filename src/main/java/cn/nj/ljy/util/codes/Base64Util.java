package cn.nj.ljy.util.codes;

import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;

import cn.nj.ljy.util.common.StringUtil;

/**
 * 16进制和64进制的转换，可用于字符串缩短。(不是Base64编码)
 *
 * 注：0开头的32位16进制字符串，比如 042a08acd0130f48968df9c522d4d3a5转为64进制，
 * 再转回16进制的时候会变成 42a08acd0130f48968df9c522d4d3a5，前面的补位0会丢失!
 *

 */
public class Base64Util {
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_";

    private static final BigInteger AND_BASE = new BigInteger("63");

    private Base64Util() {}

    /**
     * 16进制字符串转为64进制
     *
     * @param hex
     * @return
     */
    public static String hexTo64(String hex) {
        if (StringUtil.isBlank(hex)) return "";

        //二进制位数
        int binarySizeOfHex = hex.length() * 4;

        int length64 = binarySizeOfHex % 6 == 0 ? binarySizeOfHex / 6 : binarySizeOfHex / 6 + 1;

        BigInteger originHexBigInt = new BigInteger(hex, 16);

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length64; i++) {

            BigInteger sixBits = originHexBigInt.and(AND_BASE);

            sb.append(ALPHABET.charAt(sixBits.intValue()));

            originHexBigInt = originHexBigInt.shiftRight(6);
        }

        return sb.reverse().toString();
    }

    /**
     * 64进制字符串转为16进制
     * 注：原16进制的高位0会丢失
     *
     * @param str       64进制的字符串
     * @param length    转换为16进制后所需完整长度，高位补0
     * @return
     */
    public static String base64Tohex(String str, int length) {
        if (StringUtil.isBlank(str)) return "";

        //64进制位数
        int base64Size = str.length();

        BigInteger decimal = BigInteger.valueOf(0);
        for (int i = 0; i < base64Size; i++) {
            int n = ALPHABET.indexOf(str.charAt(base64Size - i - 1));

            BigInteger tmp = BigInteger.valueOf(n).multiply(BigInteger.valueOf(64).pow(i));
            decimal = decimal.add(tmp);
        }

        String hex = decimal.toString(16);

        //64进制转为16进制需要考虑补码
        //int paddingBinaryLen = base64Size * 6 - hex.length() * 4;
        //最多补一个字节
        //int paddingLen = paddingBinaryLen / 4 > 2 ? 2 : paddingBinaryLen / 4 - 1;

        return length > 0 ? StringUtils.leftPad(hex, length, "0") : hex;
    }

    public static String base64Tohex(String str) {
        return base64Tohex(str, 0);
    }

}
