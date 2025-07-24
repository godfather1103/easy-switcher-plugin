package io.github.godfather1103.easy.switcher.util

import org.apache.commons.lang3.StringUtils

/**
 *
 * Title:        Godfather1103's Github
 *
 * Copyright:    Copyright (c) 2025
 *
 * Company:      [https://github.com/godfather1103](https://github.com/godfather1103)
 * 类描述：字符串相关的工具类
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @version 1.0
 * @date 创建时间：2025/7/22 17:07
 * @since 1.0
 */
object StringUtils {
    @JvmStatic
    fun isPortOrEmpty(text: String?): Boolean {
        if (StringUtils.isEmpty(text)) {
            return true
        }
        try {
            val port = text!!.toInt()
            return port >= 0 && port <= 65535
        } catch (e: NumberFormatException) {
            return false
        }
    }

    @JvmStatic
    @JvmOverloads
    fun showString(str: String?, defaultValue: String? = ""): String? {
        return if (isEmpty(str)) {
            defaultValue
        } else {
            str!!.trim { it <= ' ' }
        }
    }

    /**
     * 显示字符串，如果为空返回空串<BR></BR>
     *
     * @param str 待处理的字符数组
     * @return 处理后的结果
     * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
     * 创建时间：2020-08-29 21:49
     */
    @JvmStatic
    fun showString(str: CharArray?): String {
        return if (str == null || str.isEmpty()) {
            ""
        } else {
            kotlin.text.String(str)
        }
    }

    /**
     * 判断字符串是否为空<BR></BR>
     *
     * @param str 待处理的字符串
     * @return 判断结果
     * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
     * 创建时间：2020-08-29 21:50
     */
    @JvmStatic
    fun isEmpty(str: String?): Boolean {
        return str == null || str.trim { it <= ' ' }.isEmpty()
    }

    @JvmStatic
    fun isNotEmpty(str: String?): Boolean {
        return !isEmpty(str)
    }

    @JvmStatic
    fun isUrl(str: String?): Boolean {
        if (isEmpty(str)) {
            return false
        }
        return str!!.startsWith("http://") || str.startsWith("https://")
    }
}
