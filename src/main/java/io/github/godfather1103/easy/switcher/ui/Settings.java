package io.github.godfather1103.easy.switcher.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.JBColor;
import io.github.godfather1103.easy.switcher.CustomProxy;
import io.github.godfather1103.easy.switcher.settings.AppSettings;
import io.github.godfather1103.easy.switcher.settings.ConfigBundle;
import io.github.godfather1103.easy.switcher.util.HttpUtils;
import io.github.godfather1103.easy.switcher.util.MyNotifier;
import io.github.godfather1103.easy.switcher.util.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
    private JCheckBox enableProxy;
    private JTextField authUserName;
    private JPasswordField authPassword;
    private JCheckBox enableAuth;
    private JTextField profileUrl;
    private JTextArea downloadProfile;
    private JTextArea customProfile;
    private JButton downProfileButton;
    private JLabel hostname;
    private JLabel port;
    private JLabel user;
    private JLabel password;
    private JLabel downloadedRules;
    private JLabel customRules;
    private JButton testConnection;

    public Settings() {
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return Optional.of(ConfigBundle.message("plugin.config.tab.title"))
                .filter(StringUtils::isNotEmpty)
                .orElse("Easy Switcher Configuration");
    }

    private void addI18Desc() {
        enableProxy.setText(ConfigBundle.message("enableProxy"));
        enableProxy.setToolTipText(ConfigBundle.message("enableProxyDesc"));

        protocol.setToolTipText(ConfigBundle.message("protocolDesc"));

        hostname.setText(ConfigBundle.message("hostname"));
        hostname.setToolTipText(ConfigBundle.message("hostname"));
        proxyHost.setToolTipText(ConfigBundle.message("hostname"));

        port.setText(ConfigBundle.message("port"));
        port.setToolTipText(ConfigBundle.message("port"));
        proxyPort.setToolTipText(ConfigBundle.message("port"));

        enableAuth.setText(ConfigBundle.message("enableAuth"));

        user.setText(ConfigBundle.message("user"));
        user.setToolTipText(ConfigBundle.message("user"));
        authUserName.setToolTipText(ConfigBundle.message("user"));

        password.setText(ConfigBundle.message("password"));
        password.setToolTipText(ConfigBundle.message("password"));
        authPassword.setToolTipText(ConfigBundle.message("password"));

        downProfileButton.setText(ConfigBundle.message("downloadProfileNow"));
        downProfileButton.setToolTipText(ConfigBundle.message("downloadProfileNow"));

        testConnection.setText(ConfigBundle.message("testConnection"));
        testConnection.setToolTipText(ConfigBundle.message("testConnection"));

        profileUrl.setToolTipText(ConfigBundle.message("profileUrl"));

        downloadedRules.setText(ConfigBundle.message("downloadProfile"));
        downloadedRules.setToolTipText(ConfigBundle.message("downloadProfile"));
        downloadProfile.setToolTipText(ConfigBundle.message("downloadProfileDesc"));

        customRules.setText(ConfigBundle.message("customProfile"));
        customRules.setToolTipText(ConfigBundle.message("customProfile"));
        customProfile.setToolTipText(ConfigBundle.message("customProfileDesc"));
    }

    private void actionEnable() {
        var selected = enableProxy.isSelected();
        protocol.setEnabled(selected);
        if (selected && protocol.getSelectedIndex() == -1) {
            protocol.setSelectedItem(Proxy.Type.HTTP.name());
        }
        proxyHost.setEnabled(selected);
        proxyPort.setEnabled(selected);
        enableAuth.setEnabled(selected);
        var authSelected = enableAuth.isSelected();
        authUserName.setEnabled(selected && authSelected);
        authPassword.setEnabled(selected && authSelected);
        downProfileButton.setEnabled(StringUtils.isUrl(profileUrl.getText()));
    }

    private AppSettings.State makeNowState() {
        return new AppSettings.State(
                enableProxy.isSelected(),
                Optional.ofNullable(protocol.getSelectedItem()).map(Objects::toString).orElse(Proxy.Type.HTTP.name()),
                proxyHost.getText(),
                proxyPort.getText(),
                enableAuth.isSelected(),
                authUserName.getText(),
                showString(authPassword.getPassword()),
                profileUrl.getText(),
                downloadProfile.getText(),
                customProfile.getText()
        );
    }

    @Override
    public @Nullable JComponent createComponent() {
        protocol.addItem(Proxy.Type.HTTP.name());
        protocol.addItem(Proxy.Type.SOCKS.name());
        addI18Desc();
        enableProxy.addChangeListener(e -> actionEnable());
        enableAuth.addChangeListener(e -> actionEnable());
        proxyPort.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!isPortOrEmpty(proxyPort.getText())) {
                    proxyPort.setText("");
                }
            }
        });
        profileUrl.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                downProfileButton.setEnabled(StringUtils.isUrl(profileUrl.getText()));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                downProfileButton.setEnabled(StringUtils.isUrl(profileUrl.getText()));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
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
        downloadProfile.setEditable(false);
        customProfile.setBorder(BorderFactory.createLineBorder(JBColor.LIGHT_GRAY));
        customProfile.setAutoscrolls(true);

        testConnection.addActionListener(e -> {
            var testConn = new TestConn(makeNowState());
            testConn.setLocationRelativeTo(rootPanel);
            testConn.pack();
            testConn.setVisible(true);
        });

        return rootPanel;
    }

    @Override
    public boolean isModified() {
        var state = AppSettings.getInstance().getState();
        return !Objects.equals(state.getEnableProxy(), enableProxy.isSelected())
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
        if (enableProxy.isSelected()) {
            if (StringUtils.isEmpty(proxyHost.getText())) {
                MyNotifier.notifyError(ConfigBundle.message("notifier.error.miss.proxy.host"));
                return;
            }
            if (!StringUtils.isPortOrEmpty(proxyPort.getText(), false)) {
                MyNotifier.notifyError(ConfigBundle.message("notifier.error.miss.proxy.port"));
                return;
            }
            if (protocol.getSelectedIndex() == -1) {
                MyNotifier.notifyError(ConfigBundle.message("notifier.error.miss.proxy.protocol"));
                return;
            }
        }
        var state = makeNowState();
        actionEnable();
        if (StringUtils.isNotEmpty(profileUrl.getText()) && StringUtils.isEmpty(downloadProfile.getText())) {
            String text = HttpUtils.downAutoproxyRule(profileUrl.getText());
            downloadProfile.setText(text);
            state.setDownloadProfile(text);
        } else if (StringUtils.isEmpty(profileUrl.getText())) {
            downloadProfile.setText("");
            state.setDownloadProfile("");
        }
        AppSettings.getInstance().getState().update(state);
        CustomProxy.Companion.reset(state);
    }

    @Override
    public void reset() {
        var state = AppSettings.getInstance().getState();
        enableProxy.setSelected(state.getEnableProxy());
        protocol.setSelectedItem(state.getProxyProtocol());
        proxyHost.setText(state.getProxyHost());
        proxyPort.setText(state.getProxyPort());
        enableAuth.setSelected(state.getEnableAuth());
        authUserName.setText(state.getAuthUserName());
        authPassword.setText(state.getAuthPassword());
        profileUrl.setText(state.getProfileUrl());
        downloadProfile.setText(state.getDownloadProfile());
        customProfile.setText(state.getCustomProfile());
        actionEnable();
        CustomProxy.Companion.reset(state);
    }
}
