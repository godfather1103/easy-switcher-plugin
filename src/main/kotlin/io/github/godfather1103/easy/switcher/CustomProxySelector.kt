package io.github.godfather1103.easy.switcher

import com.intellij.openapi.diagnostic.debug
import com.intellij.openapi.diagnostic.logger
import io.github.godfather1103.easy.switcher.settings.AppSettings
import io.github.godfather1103.easy.switcher.util.StringUtils
import io.vavr.Tuple
import io.vavr.Tuple2
import java.io.IOException
import java.net.*

internal class CustomProxySelector(
    val rules: ArrayList<ProxyRule> = ArrayList(0),
    var proxy: Proxy? = null,
    var enableAuth: Boolean = false,
    var authUserName: String? = null,
    var authPassword: String? = null
) : ProxySelector() {

    override fun select(uri: URI?): List<Proxy?>? {
        logger.debug { "$uri: select" }
        if (uri == null) {
            logger.debug { "$uri: no proxy, uri is null" }
            return emptyList()
        }
        val url = uri.toString()
        if (url == CustomProxySelector::class.java.name) {
            logger.info("$uri: no proxy, uri is default marked is init")
            return listOf(DEFAULT_PROXY)
        }
        if (proxy == null || rules.isEmpty()) {
            return emptyList()
        }
        if (!("http" == uri.scheme || "https" == uri.scheme)) {
            logger.debug { "$uri: no proxy, not http/https scheme: ${uri.scheme}" }
            return emptyList()
        }
        if (isLocalhost(uri.host ?: "")) {
            logger.debug { "$uri: no proxy, localhost" }
            return emptyList()
        }
        return rules.firstOrNull { it -> url.matches(it.ruleRegex.toRegex()) }?.run {
            logger.debug { "uri[$uri]，命中了[$ruleRegex][$needUse]" }
            if (needUse) {
                listOf(proxy)
            } else {
                emptyList()
            }
        } ?: emptyList()
    }

    fun isLocalhost(hostName: String): Boolean {
        return hostName.equals("localhost", ignoreCase = true) || hostName == "127.0.0.1" || hostName == "::1"
    }

    override fun connectFailed(uri: URI?, sa: SocketAddress?, ioe: IOException?) {

    }

    companion object {
        val DEFAULT_PROXY = Proxy(Proxy.Type.HTTP, InetSocketAddress(CustomProxySelector::class.java.name, 1))
        val INSTANCE = CustomProxySelector()
        private val logger = logger<CustomProxySelector>()

        fun reset(state: AppSettings.State) {
            // 重置
            INSTANCE.rules.clear()
            INSTANCE.proxy = null
            INSTANCE.enableAuth = state.enableAuth
            INSTANCE.authUserName = state.authUserName
            INSTANCE.authPassword = state.authPassword
            if (state.enableProxy) {
                INSTANCE.proxy = when (state.proxyProtocol) {
                    Proxy.Type.HTTP.name -> Proxy(
                        Proxy.Type.HTTP,
                        InetSocketAddress(state.proxyHost, state.proxyPort.toInt())
                    )

                    Proxy.Type.SOCKS.name -> Proxy(
                        Proxy.Type.SOCKS,
                        InetSocketAddress(state.proxyHost, state.proxyPort.toInt())
                    )

                    else -> null
                }
            }
            val exclude = ArrayList<ProxyRule>(0)
            val contains = ArrayList<ProxyRule>(0)
            if (StringUtils.isNotEmpty(state.customProfile)) {
                val t = parseRule(state.customProfile)
                exclude.addAll(t._1)
                contains.addAll(t._2)
            }
            if (StringUtils.isNotEmpty(state.downloadProfile)) {
                val t = parseRule(state.downloadProfile)
                exclude.addAll(t._1)
                contains.addAll(t._2)
            }
            // 优先追加排除规则
            INSTANCE.rules.addAll(exclude)
            INSTANCE.rules.addAll(contains)
            logger.info("加载了${INSTANCE.rules.size}条规则")
        }

        private fun parseRule(rules: String): Tuple2<List<ProxyRule>, List<ProxyRule>> {
            val exclude = ArrayList<ProxyRule>(0)
            val contains = ArrayList<ProxyRule>(0)
            rules.split("[\n\r]".toRegex()).filterNot {
                it.isEmpty() || it.startsWith("!") || it.startsWith("[")
            }.forEach {
                if (it.startsWith("@@")) {
                    exclude.add(ProxyRule(convertRuleToRegex(it.substring(2)), false))
                } else {
                    contains.add(ProxyRule(convertRuleToRegex(it)))
                }
            }
            return Tuple.of(exclude, contains)
        }

        private fun convertRuleToRegex(rule: String): String = if (rule.startsWith("||")) {
            "^(https?:\\/\\/)?([^.]+\\.)*" + rule.substring(2).replace(".", "\\.") + ".*"
        } else if (rule.startsWith("|")) {
            "^" + rule.substring(1).replace(".", "\\.")
        } else if (rule.startsWith("/") && rule.endsWith("/")) {
            rule.substring(1, rule.length - 1)
        } else {
            rule.replace(".", "\\.").replace("*", ".*")
        }
    }
}