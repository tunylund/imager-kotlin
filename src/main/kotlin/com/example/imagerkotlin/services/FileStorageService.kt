package com.example.imagerkotlin.controllers

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@Service
class FileStorageService {

    @Value("\${uploadDir}")
    private lateinit var uploadDir: String

    fun get(filename: String): File {
        return Path.of(uploadDir).resolve(filename).toFile()
    }

    fun store(file: MultipartFile): String {
        val fileName = ensureConsistentJPGSuffix(ensureSuffix(file.originalFilename!!, file.contentType))

        val targetLocation: Path = Path.of(uploadDir).resolve(fileName)
        Files.createDirectories(targetLocation.parent)
        Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)

        return fileName
    }

    private fun ensureConsistentJPGSuffix(fileName: String): String {
        return if (fileName.endsWith("jpeg")) {
            fileName.replace("jpeg", "jpg")
        } else {
            fileName
        }
    }

    private fun ensureSuffix(originalFilename: String, contentType: String?): String {
        if (contentType == null) {
            return originalFilename
        } else {
            val suffix = contentType.split("/").last()
            return if (originalFilename.endsWith(suffix)) {
                originalFilename
            } else {
                "$originalFilename.$suffix"
            }
        }
    }
}