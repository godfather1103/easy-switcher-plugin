package io.github.godfather1103.easy.switcher.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2025</p>
 * <p>Company:      <a href="https://github.com/godfather1103">https://github.com/godfather1103</a></p>
 * 类描述：
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @version 1.0
 * @date 创建时间：2025/7/22 11:28
 * @since 1.0
 */
@Service(Service.Level.APP)
@State(
        name = "io.github.godfather1103.settings.easy-switcher-plugin",
        storages = @Storage("io.github.godfather1103.settings.easy-switcher-plugin.xml")
)
public final class AppSettings implements PersistentStateComponent<AppSettings.State> {

    private State myState = new State();

    public static AppSettings getInstance() {
        return ApplicationManager.getApplication().getService(AppSettings.class);
    }

    @Override
    public @Nullable AppSettings.State getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull State state) {
        this.myState = state;
    }

    /**
     * <p>Title:        Godfather1103's Github</p>
     * <p>Copyright:    Copyright (c) 2025</p>
     * <p>Company:      <a href="https://github.com/godfather1103">https://github.com/godfather1103</a></p>
     * 类描述：数据储存
     *
     * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
     * @version 1.0
     * @date 创建时间：2025/7/24 11:46
     * @since 1.0
     */
    public static class State {
        private Boolean proxyEnable;
        private String proxyProtocol;
        private String proxyHost;
        private String proxyPort;
        private Boolean enableAuth;
        private String authUserName;
        private String authPassword;
        private String profileUrl;
        private String downloadProfile;
        private String customProfile;

        private State() {
            this.proxyEnable = false;
            this.enableAuth = false;
        }

        public Boolean getProxyEnable() {
            return proxyEnable;
        }

        public void setProxyEnable(Boolean proxyEnable) {
            this.proxyEnable = proxyEnable;
        }

        public String getProxyProtocol() {
            return proxyProtocol;
        }

        public void setProxyProtocol(String proxyProtocol) {
            this.proxyProtocol = proxyProtocol;
        }

        public String getProxyHost() {
            return proxyHost;
        }

        public void setProxyHost(String proxyHost) {
            this.proxyHost = proxyHost;
        }

        public String getProxyPort() {
            return proxyPort;
        }

        public void setProxyPort(String proxyPort) {
            this.proxyPort = proxyPort;
        }

        public Boolean getEnableAuth() {
            return enableAuth;
        }

        public void setEnableAuth(Boolean enableAuth) {
            this.enableAuth = enableAuth;
        }

        public String getAuthUserName() {
            return authUserName;
        }

        public void setAuthUserName(String authUserName) {
            this.authUserName = authUserName;
        }

        public String getAuthPassword() {
            return authPassword;
        }

        public void setAuthPassword(String authPassword) {
            this.authPassword = authPassword;
        }

        public String getProfileUrl() {
            return profileUrl;
        }

        public void setProfileUrl(String profileUrl) {
            this.profileUrl = profileUrl;
        }

        public String getDownloadProfile() {
            return downloadProfile;
        }

        public void setDownloadProfile(String downloadProfile) {
            this.downloadProfile = downloadProfile;
        }

        public String getCustomProfile() {
            return customProfile;
        }

        public void setCustomProfile(String customProfile) {
            this.customProfile = customProfile;
        }
    }
}
