package io.github.godfather1103.easy.switcher

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.util.net.JdkProxyCustomizer
import com.intellij.util.net.JdkProxyProvider
import io.github.godfather1103.easy.switcher.settings.AppSettings
import java.net.URI

internal class LoadCustomProxy : ProjectActivity {
    override suspend fun execute(project: Project) {
        val proxy = JdkProxyProvider.getInstance().proxySelector.select(URI(CustomProxySelector::class.java.name))
        if (proxy == null || proxy.isEmpty() || !proxy.contains(CustomProxySelector.DEFAULT_PROXY)) {
            JdkProxyCustomizer.getInstance().customizeProxySelector(CustomProxySelector.INSTANCE)
            AppSettings.getInstance().state?.apply {
                CustomProxySelector.reset(this)
            }
        }
    }

}