package io.github.godfather1103.easy.switcher

import io.vavr.Tuple2
import org.junit.Test

class CustomProxyTest {

    @Test
    fun testParse() {
        listOf(
            Pair("||example.com", Tuple2("http://www.example.com/foo", true)),
            Pair("||example.com", Tuple2("https://subdomain.example.com/bar", true)),
            Pair("||example.com", Tuple2("http://www.google.com/search?q=example.com", false)),
            Pair("|https://ssl.example.com", Tuple2("https://ssl.example.com/bar", true)),
            Pair("|https://ssl.example.com", Tuple2("http://ssl.example.com/bar", false)),
            Pair("|http://example.com", Tuple2("http://example.com/bar", true)),
            Pair("|http://example.com", Tuple2("https://example.com/bar", false)),
            Pair("/^https?:\\/\\/[^\\/]+example\\.com/", Tuple2("http://www.example.com", true)),
            Pair("/^https?:\\/\\/[^\\/]+example\\.com/", Tuple2("https://examxxple.com/bar", false)),
            Pair("example.com", Tuple2("http://www.example.com/foo", true)),
            Pair("example.com", Tuple2("http://www.google.com/search?q=www.example.com", true)),
            Pair("example.com", Tuple2("https://www.example.com/", false)),
        ).forEach {
            val rule = CustomProxy.convertRuleToRegex(it.first)
            assert(it.second._1.matches(rule.toRegex()) == it.second._2) {
                if (it.second._2) {
                    "${it.first}解析后的规则为$rule, ${it.second._1}应该命中"
                } else {
                    "${it.first}解析后的规则为$rule, ${it.second._1}不应该命中"
                }
            }
        }
    }
}