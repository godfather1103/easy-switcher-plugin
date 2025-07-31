package io.github.godfather1103.easy.switcher

import com.intellij.openapi.diagnostic.logger
import com.intellij.util.net.JdkProxyCustomizer
import com.intellij.util.net.JdkProxyProvider
import io.github.godfather1103.easy.switcher.settings.AppSettings
import io.github.godfather1103.easy.switcher.util.StringUtils
import io.vavr.Tuple
import io.vavr.Tuple2
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URI

internal class CustomProxy(
    val rules: ArrayList<ProxyRule> = ArrayList(0),
    var proxy: Proxy? = null,
    var enableAuth: Boolean = false,
    var authUserName: String? = null,
    var authPassword: String? = null
) {
    companion object {
        val DEFAULT_PROXY = Proxy(Proxy.Type.HTTP, InetSocketAddress(CustomProxy::class.java.name, 0))
        val INSTANCE = CustomProxy()
        private val logger = logger<CustomProxy>()

        val DEFAULT_URI = URI(CustomProxy::class.java.name)

        @JvmStatic
        fun checkRule(url: String): ProxyRule? {
            return INSTANCE.rules.firstOrNull { it -> url.matches(it.ruleRegex.toRegex()) }
        }

        @JvmStatic
        fun reset(state: AppSettings.State) {
            // 重置
            INSTANCE.rules.clear()
            INSTANCE.proxy = null
            INSTANCE.enableAuth = state.enableAuth
            INSTANCE.authUserName = state.authUserName
            INSTANCE.authPassword = state.authPassword
            if (state.enableProxy) {
                INSTANCE.proxy = runCatching {
                    when (state.proxyProtocol) {
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
                }.getOrNull()
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

            val proxy = JdkProxyProvider.getInstance().proxySelector.select(DEFAULT_URI)
            if (proxy == null || proxy.isEmpty() || !proxy.contains(DEFAULT_PROXY)) {
                JdkProxyCustomizer.getInstance().customizeProxySelector(CustomProxySelector.INSTANCE)
            }
            val pass = JdkProxyProvider.getInstance().authenticator.requestPasswordAuthenticationInstance(
                DEFAULT_URI.toString(), null, 0, null, null, null, null, null
            )
            if (pass == null) {
                JdkProxyCustomizer.getInstance().customizeAuthenticator(CustomProxyAuthenticator.INSTANCE)
            }
        }

        private fun parseRule(rules: String): Tuple2<List<ProxyRule>, List<ProxyRule>> {
            val exclude = ArrayList<ProxyRule>(0)
            val contains = ArrayList<ProxyRule>(0)
            rules.split("[\n\r]".toRegex()).filterNot {
                it.isEmpty() || it.startsWith("!") || it.startsWith("[")
            }.forEach {
                val ruleRegex: String
                if (it.startsWith("@@")) {
                    ruleRegex = convertRuleToRegex(it.substring(2))
                    exclude.add(ProxyRule(ruleRegex, false))
                } else {
                    ruleRegex = convertRuleToRegex(it)
                    contains.add(ProxyRule(ruleRegex))
                }
            }
            return Tuple.of(exclude, contains)
        }

        fun convertRuleToRegex(rule: String): String = if (rule.startsWith("||")) {
            "^(https?:\\/\\/)?([^.]+\\.)*" + rule.substring(2).replace(".", "\\.") + ".*"
        } else if (rule.startsWith("|")) {
            "^" + rule.substring(1).replace(".", "\\.") + ".*"
        } else if (rule.startsWith("/") && rule.endsWith("/")) {
            rule.substring(1, rule.length - 1)
        } else if (rule.startsWith("https://")) {
            val tmp = rule.replace(".", "\\.").replace("*", ".*")
            "^http:\\/\\/.+$tmp.*"
        } else if (rule.startsWith("http://")) {
            val tmp = rule.replace(".", "\\.").replace("*", ".*")
            "$tmp.*|^http:\\/\\/.+$tmp.*"
        } else {
            val tmp = rule.replace(".", "\\.").replace("*", ".*")
            "^http:\\/\\/.*$tmp.*"
        }

    }
}