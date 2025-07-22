package io.github.godfather1103.easy.switcher.util;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2025</p>
 * <p>Company:      <a href="https://github.com/godfather1103">https://github.com/godfather1103</a></p>
 * 类描述：字符串相关的工具类
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @version 1.0
 * @date 创建时间：2025/7/22 17:07
 * @since 1.0
 */
public class StringUtils {

    public static boolean isPortOrEmpty(String text) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(text)) {
            return true;
        }
        try {
            var port = Integer.parseInt(text);
            return port >= 0 && port <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String showString(String str) {
        return showString(str, "");
    }

    public static String showString(String str, String defaultValue) {
        if (isEmpty(str)) {
            return defaultValue;
        } else {
            return str.trim();
        }
    }

    /**
     * 显示字符串，如果为空返回空串<BR>
     *
     * @param str 待处理的字符数组
     * @return 处理后的结果
     * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
     * 创建时间：2020-08-29 21:49
     */
    public static String showString(char[] str) {
        if (str == null || str.length == 0) {
            return "";
        } else {
            return new String(str);
        }
    }

    /**
     * 判断字符串是否为空<BR>
     *
     * @param str 待处理的字符串
     * @return 判断结果
     * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
     * 创建时间：2020-08-29 21:50
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isUrl(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.startsWith("http://") || str.startsWith("https://");
    }
}
