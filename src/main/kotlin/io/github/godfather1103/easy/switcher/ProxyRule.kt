package io.github.godfather1103.easy.switcher

data class ProxyRule(
    val ruleRegex: String,
    val needUse: Boolean = true
)
