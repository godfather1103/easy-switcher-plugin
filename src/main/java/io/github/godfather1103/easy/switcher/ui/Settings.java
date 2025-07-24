package io.github.godfather1103.easy.switcher.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.JBColor;
import io.github.godfather1103.easy.switcher.CustomProxySelector;
import io.github.godfather1103.easy.switcher.settings.AppSettings;
import io.github.godfather1103.easy.switcher.settings.ConfigBundle;
import io.github.godfather1103.easy.switcher.util.HttpUtils;
import io.github.godfather1103.easy.switcher.util.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.net.Proxy;
import java.util.Objects;
import java.util.Optional;

import static io.github.godfather1103.easy.switcher.util.StringUtils.isPortOrEmpty;
import static io.github.godfather1103.easy.switcher.util.StringUtils.showString;


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
    private JTextField profileUrl;
    private JTextArea downloadProfile;
    private JTextArea customProfile;
    private JButton downProfileButton;

    public Settings() {
        proxyPort.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!isPortOrEmpty(proxyPort.getText())) {
                    proxyPort.setText("");
                }
            }
        });
        profileUrl.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                downProfileButton.setEnabled(StringUtils.isUrl(profileUrl.getText()));
            }
        });
        downProfileButton.addActionListener(e -> {
            try {
                downProfileButton.setEnabled(false);
                downloadProfile.setText(HttpUtils.downAutoproxyRule(profileUrl.getText()));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            } finally {
                downProfileButton.setEnabled(true);
            }
        });
        downloadProfile.setBorder(BorderFactory.createLineBorder(JBColor.LIGHT_GRAY));
        downloadProfile.setAutoscrolls(true);
        customProfile.setBorder(BorderFactory.createLineBorder(JBColor.LIGHT_GRAY));
        customProfile.setAutoscrolls(true);
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return Optional.ofNullable(ConfigBundle.message("display_name"))
                .filter(StringUtils::isNotEmpty)
                .orElse("Easy Switcher Configuration");
    }

    @Override
    public @Nullable JComponent createComponent() {
        protocol.addItem(Proxy.Type.HTTP.name());
        protocol.addItem(Proxy.Type.SOCKS.name());
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
        downProfileButton.setEnabled(StringUtils.isUrl(profileUrl.getText()));
    }

    @Override
    public boolean isModified() {
        var state = Objects.requireNonNull(AppSettings.getInstance().getState());
        return !Objects.equals(state.getProxyEnable(), proxyEnable.isSelected())
                || !Objects.equals(state.getProxyProtocol(), protocol.getSelectedItem())
                || !Objects.equals(state.getProxyHost(), proxyHost.getText())
                || !Objects.equals(state.getProxyPort(), proxyPort.getText())
                || !Objects.equals(state.getDownloadProfile(), downloadProfile.getText())
                || !Objects.equals(state.getProfileUrl(), profileUrl.getText())
                || !Objects.equals(state.getCustomProfile(), customProfile.getText())
                || !Objects.equals(state.getEnableAuth(), enableAuth.isSelected())
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
        state.setProfileUrl(profileUrl.getText());
        state.setDownloadProfile(downloadProfile.getText());
        state.setCustomProfile(customProfile.getText());
        actionOnSelect();
        if (StringUtils.isNotEmpty(profileUrl.getText()) && StringUtils.isEmpty(downloadProfile.getText())) {
            String text = HttpUtils.downAutoproxyRule(profileUrl.getText());
            downloadProfile.setText(text);
            state.setDownloadProfile(text);
        }
        CustomProxySelector.Companion.reset(state);
    }

    @Override
    public void reset() {
        var state = Objects.requireNonNull(AppSettings.getInstance().getState());
        proxyEnable.setSelected(state.getProxyEnable());
        protocol.setSelectedItem(state.getProxyProtocol());
        proxyHost.setText(state.getProxyHost());
        proxyPort.setText(state.getProxyPort());
        enableAuth.setSelected(state.getEnableAuth());
        authUserName.setText(state.getAuthUserName());
        authPassword.setText(state.getAuthPassword());
        profileUrl.setText(state.getProfileUrl());
        downloadProfile.setText(state.getDownloadProfile());
        customProfile.setText(state.getCustomProfile());
        actionOnSelect();
        CustomProxySelector.Companion.reset(state);
    }
}
