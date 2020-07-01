package com.zhc

import com.android.build.api.transform.*
import com.google.common.collect.Sets
import org.gradle.api.Project


class CustomTransform extends Transform {
    Project mProject

    CustomTransform(Project project) {
        mProject = project
    }

    @Override
    String getName() {
        println("zhc getName")
        return "zhc plugin"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        println("zhc getInputTypes")
        return Collections.singleton(QualifiedContent.DefaultContentType.CLASSES)
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        println("zhc getScopes")

        return Sets.immutableEnumSet(QualifiedContent.Scope.PROJECT,
                QualifiedContent.Scope.SUB_PROJECTS,
                QualifiedContent.Scope.EXTERNAL_LIBRARIES)
    }

    @Override
    boolean isIncremental() {
        println("zhc isIncremental")

        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        println("zhc transform")
        transformInvocation.inputs.each { input ->
            input.jarInputs.each { jarInput ->

                if (!jarInput.file.exists()) return
                mProject.logger.info("jar input:" + jarInput.file.getAbsolutePath())
                mProject.logger.info("jar name:" + jarInput.name)

                def jarName = jarInput.name

                def dest = transformInvocation.outputProvider.getContentLocation(jarName, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                mProject.logger.info("jar output path:" + dest.getAbsolutePath())

            }

            //dirInput为java和kotlin对应的.class文件
            input.directoryInputs.each {  dirInput ->

                mProject.logger.info("dirInput.file :" + dirInput.file)

                def outDir = transformInvocation.outputProvider.getContentLocation(dirInput.name, dirInput.contentTypes, dirInput.scopes, Format.DIRECTORY)
                mProject.logger.info("dir output path:" + outDir.absolutePath)
                //变化后的class文件的目录?
                //zhc dirInput : D:\projects\plugin\app\build\intermediates\transforms\zhc plugin\release\32
                println("zhc dirInput : " + outDir.absolutePath)

                //对文件目录下的class文件遍历处理
                //zhc dirInput file -2 : D:\projects\plugin\app\build\intermediates\javac\debug\classes
                //zhc dirInput file -2 : D:\projects\plugin\app\build\tmp\kotlin-classes\debug
                if (dirInput.changedFiles != null && !dirInput.changedFiles.isEmpty()) {
                    dirInput.changedFiles.keySet().each({ changedFile ->
                        println("zhc dirInput change: " + changedFile.absolutePath)
                    })
                }
                if (dirInput.file != null && dirInput.file.exists()) {
                dirInput.file.absolutePath
                    println("zhc dirInput file -2 : " + dirInput.file.absolutePath)
                }
            }


        }
    }
}