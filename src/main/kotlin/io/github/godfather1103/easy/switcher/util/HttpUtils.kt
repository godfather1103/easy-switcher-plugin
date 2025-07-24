package io.github.godfather1103.easy.switcher.util

import io.github.godfather1103.easy.switcher.settings.ConfigBundle.message
import io.github.godfather1103.easy.switcher.util.StringUtils.isNotEmpty
import io.github.godfather1103.easy.switcher.util.StringUtils.isUrl
import io.vavr.control.Try
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.apache.commons.codec.binary.Base64
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import java.util.function.Function

/**
 *
 * Title:        Godfather1103's Github
 *
 * Copyright:    Copyright (c) 2025
 *
 * Company:      [https://github.com/godfather1103](https://github.com/godfather1103)
 * 类描述：HTTP工具
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @version 1.0
 * @date 创建时间：2025/7/22 17:07
 * @since 1.0
 */
object HttpUtils {
    private val CLIENT = OkHttpClient.Builder()
        .callTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    @JvmStatic
    fun downAutoproxyRule(url: String): String {
        if (isUrl(url)) {
            val request = Request.Builder()
                .url(url)
                .get()
                .build()
            return Try.of {
                val response = execute(request)
                if (isNotEmpty(response)) {
                    if (response.startsWith("W0F1dG9Qcm94")) {
                        return@of String(Base64.builder().get().decode(response), StandardCharsets.UTF_8)
                    } else if (response.startsWith("[AutoProxy")) {
                        return@of response
                    }
                }
                return@of ""
            }.getOrElseThrow(Function { e: Throwable -> RuntimeException(e) })
        }
        return ""
    }

    @Throws(IOException::class)
    fun execute(request: Request): String {
        val response = exec(request)
        if (response.isSuccessful) {
            return response.body!!.string()
        } else {
            val msg = String.format(message("network_error") + " Url[%s],Code[%s]", request.url, response.code)
            throw RuntimeException(msg)
        }
    }

    @Throws(IOException::class)
    fun exec(request: Request): Response {
        return CLIENT.newCall(request).execute()
    }
}
