package io.github.godfather1103.easy.switcher.settings

import com.intellij.DynamicBundle
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.PropertyKey

object ConfigBundle {

    private const val BUNDLE: String = "messages.config"
    private val INSTANCE: DynamicBundle = DynamicBundle(ConfigBundle::class.java, BUNDLE)

    @JvmStatic
    fun message(@NotNull @PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any) =
        INSTANCE.getMessage(key, *params)
}