package io.github.godfather1103.easy.switcher

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.util.net.JdkProxyCustomizer
import com.intellij.util.net.JdkProxyProvider
import io.github.godfather1103.easy.switcher.settings.AppSettings

internal class LoadCustomProxy : ProjectActivity {
    override suspend fun execute(project: Project) {
        CustomProxy.reset(AppSettings.instance.state)
        val proxy = JdkProxyProvider.getInstance().proxySelector.select(CustomProxy.DEFAULT_URI)
        if (proxy == null || proxy.isEmpty() || !proxy.contains(CustomProxy.DEFAULT_PROXY)) {
            JdkProxyCustomizer.getInstance().customizeProxySelector(CustomProxySelector.INSTANCE)
        }
        val pass = JdkProxyProvider.getInstance().authenticator.requestPasswordAuthenticationInstance(
            CustomProxy.DEFAULT_URI.toString(), null, 0, null, null, null, null, null
        )
        if (pass == null) {
            JdkProxyCustomizer.getInstance().customizeAuthenticator(CustomProxyAuthenticator.INSTANCE)
        }
    }

}