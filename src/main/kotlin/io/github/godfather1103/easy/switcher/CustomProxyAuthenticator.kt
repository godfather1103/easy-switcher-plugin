package io.github.godfather1103.easy.switcher

import com.intellij.util.net.getHostNameReliably
import io.ktor.util.network.*
import java.net.Authenticator
import java.net.PasswordAuthentication

class CustomProxyAuthenticator : Authenticator() {

    override fun getPasswordAuthentication(): PasswordAuthentication? {
        val isProxy = RequestorType.PROXY == requestorType || "SOCKS authentication" == requestingPrompt
        if (!isProxy) {
            return null
        }
        // 没有代理服务器或者规则为空或者关闭了认证
        if (CustomProxy.INSTANCE.proxy == null || CustomProxy.INSTANCE.rules.isEmpty() || !CustomProxy.INSTANCE.enableAuth) {
            return null
        }
        val host = getHostNameReliably(requestingHost, requestingSite, requestingURL) ?: ""
        val port = requestingPort
        val proxy = CustomProxy.INSTANCE.proxy!!
        if (proxy.type().name != requestingProtocol) {
            return null
        } else if (proxy.address().address != host) {
            return null
        } else if (proxy.address().port != port) {
            return null
        }
        return CustomProxy.INSTANCE.authUserName?.let {
            PasswordAuthentication(it, CustomProxy.INSTANCE.authPassword?.toCharArray() ?: CharArray(0))
        }
    }

    companion object {
        val INSTANCE = CustomProxyAuthenticator()
    }
}