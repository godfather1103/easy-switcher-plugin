package io.github.godfather1103.easy.switcher.util

import com.intellij.notification.NotificationGroupManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory


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
     * 异常通知<BR>
     * @param msg 参数
     * @return 结果
     * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
     * @date 创建时间：2025/7/24 15:59
     */
    @JvmStatic
    @JvmOverloads
    fun notifyError(msg: String, project: Project? = null) {
        JBPopupFactory.getInstance().createMessage(msg).showInFocusCenter()
        NotificationGroupManager.getInstance()
            .getNotificationGroup("io.github.godfather1103.easy-switcher-plugin.notificationGroup")
            .createNotification(msg, com.intellij.notification.NotificationType.ERROR)
            .notify(project)
    }

    /**
     * 正常通知<BR>
     * @param msg 参数
     * @return 结果
     * @author 作者: chu.chuanbao E-mail: chu.chuanbao@trs.com.cn
     * @date 创建时间：2025/7/25 11:59
     */
    @JvmStatic
    @JvmOverloads
    fun notifyInfo(msg: String, project: Project? = null) {
        JBPopupFactory.getInstance().createMessage(msg).showInFocusCenter()
        NotificationGroupManager.getInstance()
            .getNotificationGroup("io.github.godfather1103.easy-switcher-plugin.notificationGroup")
            .createNotification(msg, com.intellij.notification.NotificationType.INFORMATION)
            .notify(project)
    }
}