package io.github.godfather1103.easy.switcher.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import java.net.Proxy

@Service(Service.Level.APP)
@State(
    name = "io.github.godfather1103.settings.easy-switcher-plugin",
    storages = [Storage("io.github.godfather1103.settings.easy-switcher-plugin.xml")]
)
class AppSettings(
    private var state: State = State()
) : PersistentStateComponent<AppSettings.State> {
    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    companion object {
        @JvmStatic
        val instance
            get() = ApplicationManager.getApplication().getService<AppSettings>(AppSettings::class.java)!!
    }

    data class State(
        var enableProxy: Boolean = false,
        var proxyProtocol: String = Proxy.Type.HTTP.name,
        var proxyHost: String = "",
        var proxyPort: String = "",
        var enableAuth: Boolean = false,
        var authUserName: String = "",
        var authPassword: String = "",
        var profileUrl: String = "",
        var downloadProfile: String = "",
        var customProfile: String = ""
    ) {
        fun update(now: State): AppSettings.State {
            this.enableProxy = now.enableProxy
            this.proxyProtocol = now.proxyProtocol
            this.proxyHost = now.proxyHost
            this.proxyPort = now.proxyPort
            this.enableAuth = now.enableAuth
            this.authUserName = now.authUserName
            this.authPassword = now.authPassword
            this.profileUrl = now.profileUrl
            this.downloadProfile = now.downloadProfile
            this.customProfile = now.customProfile
            return this
        }
    }
}