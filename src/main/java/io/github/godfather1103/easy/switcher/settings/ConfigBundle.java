package io.github.godfather1103.easy.switcher.settings;

import com.intellij.DynamicBundle;
import com.intellij.ide.IdeDeprecatedMessagesBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.function.Supplier;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2025</p>
 * <p>Company:      <a href="https://github.com/godfather1103">https://github.com/godfather1103</a></p>
 * 类描述：
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @version 1.0
 * @date 创建时间：2025/7/22 16:10
 * @since 1.0
 */
public final class ConfigBundle {
    public static final String BUNDLE = "messages.config";
    private static final DynamicBundle INSTANCE = new DynamicBundle(ConfigBundle.class, BUNDLE);

    public static @NotNull @Nls String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, Object @NotNull ... params) {
        return INSTANCE.containsKey(key) ? INSTANCE.getMessage(key, params) : IdeDeprecatedMessagesBundle.message(key, params);
    }

    public static @NotNull Supplier<@Nls String> messagePointer(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, Object @NotNull ... params) {
        return INSTANCE.containsKey(key) ? INSTANCE.getLazyMessage(key, params) : IdeDeprecatedMessagesBundle.messagePointer(key, params);
    }

    private ConfigBundle() {
    }
}
