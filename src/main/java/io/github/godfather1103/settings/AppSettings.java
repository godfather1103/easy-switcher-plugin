package io.github.godfather1103.settings;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2025</p>
 * <p>Company:      <a href="https://github.com/godfather1103">https://github.com/godfather1103</a></p>
 * 类描述：
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @version 1.0
 * @date 创建时间：2025/7/22 11:28
 * @since 1.0
 */
@Service(Service.Level.APP)
@State(
        name = "io.github.godfather1103.settings.easy-switch-plugin",
        storages = @Storage("io.github.godfather1103.settings.easy-switch-plugin.xml")
)
public final class AppSettings {
}
