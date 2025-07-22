package io.github.godfather1103.easy.switcher.util;

import io.github.godfather1103.easy.switcher.settings.ConfigBundle;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2025</p>
 * <p>Company:      <a href="https://github.com/godfather1103">https://github.com/godfather1103</a></p>
 * 类描述：HTTP工具
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @version 1.0
 * @date 创建时间：2025/7/22 17:07
 * @since 1.0
 */
public class HttpUtils {

    private final static OkHttpClient CLIENT = new OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build();

    public static String downAutoproxyRule(String url) {
        if (StringUtils.isUrl(url)) {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            return Try.of(() -> {
                String response = HttpUtils.execute(request);
                if (StringUtils.isNotEmpty(response)) {
                    if (response.startsWith("W0F1dG9Qcm94")) {
                        return new String(Base64.builder().get().decode(response), StandardCharsets.UTF_8);
                    } else if (response.startsWith("[AutoProxy")) {
                        return response;
                    }
                }
                return "";
            }).getOrElseThrow(e -> new RuntimeException(e));
        }
        return "";
    }

    public static String execute(Request request) throws IOException {
        Response response = exec(request);
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            String msg = String.format(ConfigBundle.message("network_error") + " Url[%s],Code[%s]", request.url(), response.code());
            throw new RuntimeException(msg);
        }
    }

    public static Response exec(Request request) throws IOException {
        return CLIENT.newCall(request).execute();
    }

    public static Tuple2<Boolean, Integer> checkNetwork(@NotNull String url) {
        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("URL is Empty!");
        }
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = exec(request);
            return new Tuple2<>(response.isSuccessful(), response.code());
        } catch (IOException e) {
            throw new RuntimeException(ConfigBundle.message("network_error"));
        }
    }
}
