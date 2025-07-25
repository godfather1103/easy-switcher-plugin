package io.github.godfather1103.easy.switcher.ui;

import com.intellij.ui.JBColor;
import com.intellij.util.net.HttpConnectionUtils;
import io.github.godfather1103.easy.switcher.CustomProxy;
import io.github.godfather1103.easy.switcher.settings.AppSettings;
import io.github.godfather1103.easy.switcher.settings.ConfigBundle;
import io.github.godfather1103.easy.switcher.util.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2025</p>
 * <p>Company:      <a href="https://github.com/godfather1103">https://github.com/godfather1103</a></p>
 * 类描述：
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @version 1.0
 * @date 创建时间：2025/7/25 12:33
 * @since 1.0
 */
public class TestConn extends JDialog {
    private JPanel contentPane;
    private JButton buttonOk;
    private JButton buttonCancel;
    private JTextField url;
    private JLabel showInfo;
    private final AppSettings.State state;

    public TestConn(AppSettings.State state) {
        this.state = state;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOk);
        url.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                buttonOk.setEnabled(StringUtils.isUrl(url.getText()));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                buttonOk.setEnabled(StringUtils.isUrl(url.getText()));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                buttonOk.setEnabled(StringUtils.isUrl(url.getText()));
            }
        });
        buttonOk.addActionListener(e -> onOk());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOk() {
        String myUrl = url.getText();
        if (!StringUtils.isUrl(myUrl)) {
            showInfo.setText(ConfigBundle.message("notifier.error.testConnection.invalid.url"));
            return;
        }
        try {
            // 注入自定义规则
            state.setCustomProfile("|" + myUrl);
            // 重置为当前规则
            CustomProxy.reset(state);
            HttpConnectionUtils.prepareUrl(myUrl);
            showInfo.setText(ConfigBundle.message("network_ok"));
            showInfo.setForeground(JBColor.GREEN);
        } catch (IOException e) {
            showInfo.setText(ConfigBundle.message("network_error"));
            showInfo.setForeground(JBColor.RED);
        } finally {
            // 还原默认
            CustomProxy.reset(AppSettings.getInstance().getState());
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
