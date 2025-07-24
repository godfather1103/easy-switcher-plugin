package io.github.godfather1103.easy.switcher.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurableProvider
import io.github.godfather1103.easy.switcher.ui.Settings

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2025</p>
 * <p>Company:      <a href="https://github.com/godfather1103">https://github.com/godfather1103</a></p>
 * 类描述：
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @version 1.0
 * @date 创建时间：2025/7/22 12:03
 * @since 1.0
 */
class AppSettingsConfigurableProvider : ConfigurableProvider() {
    override fun createConfigurable(): Configurable = Settings()
}