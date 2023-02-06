package com.iserver.common.core.utils;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类(根据自己项目自行处理和替换)
 *
 * @author Alay
 * @date 2021-11-11 12:37
 */
@UtilityClass
public class IStrUtil {

    private static final Pattern LINE_PATTERN = Pattern.compile("_(\\w)");
    private static final Pattern HUMP_PATTERN = Pattern.compile("[A-Z]");


    /**
     * 下划线转 小驼峰
     */
    public String line2Hump(String str) {
        str = str.toLowerCase();
        Matcher matcher = LINE_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    /**
     * 首字母转大写
     *
     * @param str
     * @return
     */
    public static String firstUpCase(String str) {
        int strLen;
        return str != null && (strLen = str.length()) != 0 ? (new StringBuffer(strLen)).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).toString() : str;
    }

    /**
     * 首字母转小写
     *
     * @param str
     * @return
     */
    public static String firstLowCase(String str) {
        int strLen;
        return str != null && (strLen = str.length()) != 0 ? (new StringBuffer(strLen)).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString() : str;
    }

    /**
     * 驼峰转下划线
     */
    public String hump2Line(String str) {
        // 首字母转小写
        str = str.substring(0, 1).toLowerCase() + str.substring(1);
        Matcher matcher = HUMP_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    /**
     * 表名转换成Java类名
     *
     * @param str    字符串
     * @param prefix 前缀
     * @return
     */
    public String subPrefix(String str, String prefix) {
        if (StrUtil.isBlank(str)) {
            return str;
        }
        if (StrUtil.isNotBlank(prefix)) {
            // 将表前缀去掉
            str = str.replaceFirst(prefix, "");
        }
        return str;
    }


}
