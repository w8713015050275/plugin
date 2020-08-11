package com.zhangmen.aptprocessor

import com.zhangmen.libanotation.MyAnotation
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.StandardLocation

@SupportedSourceVersion(SourceVersion.RELEASE_8)
class AptProcessor: AbstractProcessor() {

    private var messager: Messager? = null
    private var filer: Filer? = null
    private val fileName = "aa.json"

    override fun init(p0: ProcessingEnvironment?) {
        println("zhc apt init")
        super.init(p0)
        p0?.let {
            messager = it.messager
            filer = it.filer
        }
    }

    override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {
        println("zhc apt process")
        System.out.println("zhc<<<<<<<<<<"+ "hahahahahahha")
        val defaultFilePath = filer?.getResource(
            StandardLocation.CLASS_OUTPUT,
            "",
            fileName
        )?.toUri()?.path
        System.out.println("<<<<<<<<<<"+defaultFilePath)
        val tempDirPath = defaultFilePath?.substring(0, defaultFilePath.indexOf("app") + 4)
        val dirPath = tempDirPath + "src/main/assets"
        System.out.println(dirPath)
        val dirFile = File(dirPath)
        if (!dirFile.exists()) {
            File(dirPath).mkdirs()
        }
        return false
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val types = LinkedHashSet<String>()
        types.add(MyAnotation::class.java.canonicalName)
        return types
    }
}