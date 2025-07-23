package io.github.godfather1103.easy.switcher

import com.intellij.ide.ApplicationInitializedListener
import com.intellij.openapi.diagnostic.debug
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.util.net.NO_PROXY_LIST
import java.io.IOException
import java.net.Proxy
import java.net.ProxySelector
import java.net.SocketAddress
import java.net.URI

class CustomProxySelector : ProxySelector() {
    override fun select(uri: URI?): List<Proxy?>? {
        logger.debug { "$uri: select" }
        if (uri == null) {
            logger.debug { "$uri: no proxy, uri is null" }
            return NO_PROXY_LIST
        }
        if (!("http" == uri.scheme || "https" == uri.scheme)) {
            logger.debug { "$uri: no proxy, not http/https scheme: ${uri.scheme}" }
            return NO_PROXY_LIST
        }
        if (isLocalhost(uri.host ?: "")) {
            logger.debug { "$uri: no proxy, localhost" }
            return NO_PROXY_LIST
        }
        TODO("Not yet implemented")
    }

    fun isLocalhost(hostName: String): Boolean {
        return hostName.equals("localhost", ignoreCase = true) || hostName == "127.0.0.1" || hostName == "::1"
    }

    override fun connectFailed(uri: URI?, sa: SocketAddress?, ioe: IOException?) {
        TODO("Not yet implemented")
    }

    companion object {

        private val logger = logger<CustomProxySelector>()

    }
}

private class LoadCustomProxy : ProjectActivity, ProjectManagerListener {
    override suspend fun execute(project: Project) {
        TODO("Not yet implemented")
    }

}