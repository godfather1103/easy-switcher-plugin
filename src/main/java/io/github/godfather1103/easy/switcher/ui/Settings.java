package io.github.godfather1103.easy.switcher.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.NlsContexts;
import io.github.godfather1103.easy.switcher.settings.AppSettings;
import io.github.godfather1103.easy.switcher.settings.ConfigBundle;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Objects;
import java.util.Optional;


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

    private JComboBox protocol;
    private JPanel rootPanel;
    private JTextField proxyHost;
    private JTextField proxyPort;
    private JCheckBox proxyEnable;
    private JTextField authUserName;
    private JPasswordField authPassword;
    private JCheckBox enableAuth;

    public Settings() {
        proxyPort.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!isPortOrEmpty(proxyPort.getText())) {
                    proxyPort.setText("");
                }
            }
        });
    }

    private static boolean isPortOrEmpty(String text) {
        if (StringUtils.isEmpty(text)) {
            return true;
        }
        try {
            var port = Integer.parseInt(text);
            return port >= 0 && port <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private static String showString(char[] str) {
        if (str == null || str.length == 0) {
            return "";
        } else {
            return new String(str);
        }
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return Optional.ofNullable(ConfigBundle.message("display_name"))
                .filter(StringUtils::isNotEmpty)
                .orElse("Easy Switcher Configuration");
    }

    @Override
    public @Nullable JComponent createComponent() {
        protocol.addItem("HTTP");
        protocol.addItem("HTTPS");
        protocol.addItem("SOCKS4");
        protocol.addItem("SOCKS5");
        proxyEnable.addChangeListener(e -> actionOnSelect());
        enableAuth.addChangeListener(e -> actionOnSelect());
        return rootPanel;
    }

    private void actionOnSelect() {
        var select = proxyEnable.isSelected();
        protocol.setEnabled(select);
        proxyHost.setEnabled(select);
        proxyPort.setEnabled(select);
        enableAuth.setEnabled(select);
        var select2 = enableAuth.isSelected();
        authUserName.setEnabled(select && select2);
        authPassword.setEnabled(select && select2);
    }

    @Override
    public boolean isModified() {
        var state = Objects.requireNonNull(AppSettings.getInstance().getState());
        return !Objects.equals(state.isProxyEnable(), proxyEnable.isSelected())
                || !Objects.equals(state.getProxyProtocol(), protocol.getSelectedItem())
                || !Objects.equals(state.getProxyHost(), proxyHost.getText())
                || !Objects.equals(state.getProxyPort(), proxyPort.getText())
                || !Objects.equals(state.isEnableAuth(), enableAuth.isSelected())
                || !Objects.equals(state.getAuthUserName(), authUserName.getText())
                || !Objects.equals(state.getAuthPassword(), showString(authPassword.getPassword()));
    }

    @Override
    public void apply() {
        var state = Objects.requireNonNull(AppSettings.getInstance().getState());
        state.setProxyEnable(proxyEnable.isSelected());
        state.setProxyProtocol((String) protocol.getSelectedItem());
        state.setProxyHost(proxyHost.getText());
        state.setProxyPort(proxyPort.getText());
        state.setEnableAuth(enableAuth.isSelected());
        state.setAuthUserName(authUserName.getText());
        state.setAuthPassword(showString(authPassword.getPassword()));
        actionOnSelect();
    }

    @Override
    public void reset() {
        var state = Objects.requireNonNull(AppSettings.getInstance().getState());
        proxyEnable.setSelected(state.isProxyEnable());
        protocol.setSelectedItem(state.getProxyProtocol());
        proxyHost.setText(state.getProxyHost());
        proxyPort.setText(state.getProxyPort());
        enableAuth.setSelected(state.isEnableAuth());
        authUserName.setText(state.getAuthUserName());
        authPassword.setText(state.getAuthPassword());
        actionOnSelect();
    }
}
