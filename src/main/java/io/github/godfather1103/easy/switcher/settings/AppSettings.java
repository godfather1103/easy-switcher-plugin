package io.github.godfather1103.easy.switcher.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import lombok.Data;
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

    @Data
    public static class State {
        private boolean proxyEnable;
        private String proxyProtocol;
        private String proxyHost;
        private String proxyPort;
        private boolean enableAuth;
        private String authUserName;
        private String authPassword;

        public State() {
            this.proxyEnable = false;
            this.enableAuth = false;
        }
    }
}
