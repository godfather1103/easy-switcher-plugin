package io.github.godfather1103.easy.switcher

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import io.github.godfather1103.easy.switcher.settings.AppSettings

internal class LoadCustomProxy : ProjectActivity {
    override suspend fun execute(project: Project) {
        CustomProxy.reset(AppSettings.instance.state)
    }

}