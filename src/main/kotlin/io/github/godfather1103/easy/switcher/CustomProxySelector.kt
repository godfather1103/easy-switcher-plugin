package io.github.godfather1103.easy.switcher

import com.intellij.openapi.diagnostic.debug
import com.intellij.openapi.diagnostic.logger
import io.github.godfather1103.easy.switcher.util.HttpUtils
import java.io.IOException
import java.net.Proxy
import java.net.ProxySelector
import java.net.SocketAddress
import java.net.URI

internal class CustomProxySelector : ProxySelector() {

    override fun select(uri: URI?): List<Proxy?>? {
        logger.debug { "$uri: select" }
        if (uri == null) {
            logger.debug { "$uri: no proxy, uri is null" }
            return emptyList()
        }
        val url = uri.toString()
        if (CustomProxy.DEFAULT_URI == uri) {
            logger.info("$uri: no proxy, uri is default marked is init")
            return listOf(CustomProxy.DEFAULT_PROXY)
        }
        if (CustomProxy.INSTANCE.proxy == null || CustomProxy.INSTANCE.rules.isEmpty()) {
            return emptyList()
        }
        if (!("http" == uri.scheme || "https" == uri.scheme)) {
            logger.debug { "$uri: no proxy, not http/https scheme: ${uri.scheme}" }
            return emptyList()
        }
        if (HttpUtils.isLocalhost(uri.host ?: "")) {
            logger.debug { "$uri: no proxy, localhost" }
            return emptyList()
        }
        return CustomProxy.checkRule(url)?.run {
            logger.debug { "uri[$uri]，命中了[$ruleRegex][$needUse]" }
            if (needUse) {
                listOf(CustomProxy.INSTANCE.proxy)
            } else {
                emptyList()
            }
        } ?: emptyList()
    }

    override fun connectFailed(uri: URI?, sa: SocketAddress?, ioe: IOException?) {

    }

    companion object {
        val INSTANCE = CustomProxySelector()
        private val logger = logger<CustomProxySelector>()
    }
}