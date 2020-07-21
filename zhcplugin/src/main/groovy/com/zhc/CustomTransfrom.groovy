package com.zhc

import com.android.build.api.transform.*
import com.google.common.collect.Sets
import com.google.gson.Gson
import groovy.json.JsonOutput
import org.gradle.api.Project
import org.apache.commons.io.FileUtils
import org.objectweb.asm.*


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
                println("zhc jar input:" + jarInput.file.getAbsolutePath())
                println("zhc jar name:" + jarInput.name)

                def jarName = jarInput.name

                //根据输入的到输出
                println("zhc jarInput info: " + JsonOutput.toJson(jarInput))
                def dest = transformInvocation.outputProvider.getContentLocation(jarName, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                println("zhc jar output path:" + dest.getAbsolutePath())
                FileUtils.copyFile(jarInput.file, dest)

            }

            //dirInput为java和kotlin对应的.class文件
            input.directoryInputs.each {  dirInput ->

               println("zhc dir Input.file :" + dirInput.file)
                println("zhc dirInput info: " + JsonOutput.toJson(dirInput))

                def outDir = transformInvocation.outputProvider.getContentLocation(dirInput.name, dirInput.contentTypes, dirInput.scopes, Format.DIRECTORY)
               println("zhc dir output path:" + outDir.absolutePath)
                //变化后的class文件的目录?
                //zhc dirInput : D:\projects\plugin\app\build\intermediates\transforms\zhc plugin\release\32

                //对文件目录下的class文件遍历处理
                //zhc dirInput file -2 : D:\projects\plugin\app\build\intermediates\javac\debug\classes
                //zhc dirInput file -2 : D:\projects\plugin\app\build\tmp\kotlin-classes\debug
                if (dirInput.changedFiles != null && !dirInput.changedFiles.isEmpty()) {
                    dirInput.changedFiles.keySet().each({ changedFile ->
                        println("zhc dirInput change: " + changedFile.absolutePath)
                    })
                }
                int pathBitLen = dirInput.file.toString().length()

                def callback = { File it ->
                    println("zhc file need to transform: " + it.absolutePath)
                    //将transform后的class文件存放到output目录, transforms文件夹里面
                    if (it.exists()) {
                        def path = "${it.toString().substring(pathBitLen)}"
                        if (it.isDirectory()) {
                            new File(outDir, path).mkdirs()
                        } else {
                            def output = new File(outDir, path)
                            //这里去处理注解, output是个文件哦
                            findAnnotatedClasses(it, output)
                            if (!output.parentFile.exists()) output.parentFile.mkdirs()
                            output.bytes = it.bytes
                        }
                    }
                }

                if (dirInput.file != null && dirInput.file.exists()) {
                    dirInput.file.absolutePath
                    //编译后的class文件目录
                    println("zhc dirInput file -2 : " + dirInput.file.absolutePath)
                }


                if (dirInput.file != null && dirInput.file.exists()) {
                    //转化class目录下的class文件
                    dirInput.file.traverse(callback)
                }
            }


        }
    }

    boolean findAnnotatedClasses(File file, File output) {
        if (!file.exists() || !file.name.endsWith(".class")) {
            return
        }

        //读class文件到ClassReader, 统计注解信息
        def inputStream = new FileInputStream(file)
        ClassReader cr = new ClassReader(inputStream)
        cr.accept(new ClassVisitor(Opcodes.ASM5) {
            @Override
            AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                println("zhc visitAnnotation: " + desc)
                return super.visitAnnotation(desc, visible)
            }

            @Override
            void visitEnd() {
                super.visitEnd()
            }
        }, 0)
        inputStream.close()
    }
}