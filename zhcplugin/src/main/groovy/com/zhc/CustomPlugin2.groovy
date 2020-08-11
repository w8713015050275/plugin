package com.zhc

import com.android.build.api.dsl.extension.AndroidExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class CustomPlugin2 implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("zhc I AM A PLUGIN, register a CustomTransform2")
        project.android.registerTransform(new CustomTransform2(project))

    }
}