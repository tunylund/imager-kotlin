package com.example.imagerkotlin

import org.springframework.core.io.ClassPathResource
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

fun prepareTestFile(sourceFileName: String, uploadDir: String): Path {
    return prepareTestFile(sourceFileName, sourceFileName, uploadDir)
}

fun prepareTestFile(sourceFileName: String, targetFileName: String, uploadDir: String): Path {
    val srcFile = ClassPathResource(sourceFileName).file
    val dstPath = Path.of("${uploadDir}${targetFileName}")
    Files.copy(srcFile.toPath(), dstPath, StandardCopyOption.REPLACE_EXISTING)
    return dstPath
}
