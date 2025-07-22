package io.github.godfather1103.easy.switcher.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.NlsContexts;
import io.github.godfather1103.easy.switcher.settings.AppSettings;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2025</p>
 * <p>Company:      <a href="https://github.com/godfather1103">https://github.com/godfather1103</a></p>
 * 类描述：
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @version 1.0
 * @date 创建时间：2025/7/22 12:05
 * @since 1.0
 */
public class Settings implements Configurable {

    private final ResourceBundle bundle;
    private JComboBox protocol;
    private JPanel rootPanel;
    private JTextField proxyHost;
    private JTextField proxyPort;
    private JCheckBox proxyEnable;

    public Settings() {
        this.bundle = ResourceBundle.getBundle("i18n/describe");
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return Optional.of(bundle.getString("display_name"))
                .filter(StringUtils::isNotEmpty)
                .orElse("Easy Switcher Configuration");
    }

    @Override
    public @Nullable JComponent createComponent() {
        protocol.addItem("HTTP");
        protocol.addItem("HTTPS");
        protocol.addItem("SOCKS4");
        protocol.addItem("SOCKS5");
        proxyEnable.addChangeListener(e -> {
            actionOnSelect(proxyEnable.isSelected());
        });
        return rootPanel;
    }

    private void actionOnSelect(boolean select) {
        protocol.setEnabled(select);
        proxyHost.setEnabled(select);
        proxyPort.setEnabled(select);
    }

    @Override
    public boolean isModified() {
        var state = Objects.requireNonNull(AppSettings.getInstance().getState());
        if (!Objects.equals(state.isProxyEnable(), proxyEnable.isSelected())) {
            return true;
        } else if (!Objects.equals(state.getProxyProtocol(), protocol.getSelectedItem())) {
            return true;
        } else if (!Objects.equals(state.getProxyHost(), proxyHost.getText())) {
            return true;
        } else {
            return !Objects.equals(state.getProxyPort(), proxyPort.getText());
        }
    }

    @Override
    public void apply() {
        var state = Objects.requireNonNull(AppSettings.getInstance().getState());
        state.setProxyEnable(proxyEnable.isSelected());
        state.setProxyProtocol((String) protocol.getSelectedItem());
        state.setProxyHost(proxyHost.getText());
        state.setProxyPort(proxyPort.getText());
        actionOnSelect(proxyEnable.isSelected());
    }

    @Override
    public void reset() {
        var state = Objects.requireNonNull(AppSettings.getInstance().getState());
        proxyEnable.setSelected(state.isProxyEnable());
        protocol.setSelectedItem(state.getProxyProtocol());
        proxyHost.setText(state.getProxyHost());
        proxyPort.setText(state.getProxyPort());
        actionOnSelect(proxyEnable.isSelected());
    }
}
