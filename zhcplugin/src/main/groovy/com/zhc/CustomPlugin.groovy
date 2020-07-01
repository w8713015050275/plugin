package com.zhc

import com.android.build.api.dsl.extension.AndroidExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class CustomPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("zhc I AM A PLUGIN, register a CustomTransform")
        project.android.registerTransform(new CustomTransform(project))

    }
}