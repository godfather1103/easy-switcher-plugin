package io.github.godfather1103.easy.switcher.util

import com.intellij.notification.NotificationGroupManager

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2025</p>
 * <p>Company:      <a href="https://github.com/godfather1103">https://github.com/godfather1103</a></p>
 * 类描述：
 *
 * @author  作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @date 创建时间：2025/7/24 15:59
 * @version 1.0
 * @since  1.0
 */
object MyNotifier {

    /**
     * 通知<BR>
     * @param msg 参数
     * @return 结果
     * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
     * @date 创建时间：2025/7/24 15:59
     */
    @JvmStatic
    fun notifyError(msg: String) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("io.github.godfather1103.easy-switcher-plugin.notificationGroup")
            .createNotification(msg, com.intellij.notification.NotificationType.ERROR)
            .notify(null)
    }
}