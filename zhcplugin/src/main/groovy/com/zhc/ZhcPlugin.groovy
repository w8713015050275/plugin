package com.zhc

import org.gradle.api.Plugin
import org.gradle.api.Project

class ZhcPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("zhc I AM A PLUGIN")
        project.task("zhcTask").doLast {
            println('ZHC I AM DOING TASK')
        }

    }
}