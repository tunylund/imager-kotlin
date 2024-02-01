package com.example.imagerkotlin.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@Service
class FileStorageService {

    private val logger: Logger = LoggerFactory.getLogger(FileStorageService::class.java)

    @Value("\${uploadDir}")
    private lateinit var uploadDir: String

    fun get(filename: String): File {
        val file = Path.of(uploadDir).resolve(filename).toFile()
        if (!file.exists()) {
            throw FileNotFoundException("File $filename not found")
        }
        return file
    }

    fun get(filename: String, parsedResizeParams: ResizeService.ResizeParams): File {
        val file = File(filename)
        val fileWithSuffix = Path.of(uploadDir).resolve("${file.nameWithoutExtension}-${parsedResizeParams}.${file.extension}").toFile()
        if (!fileWithSuffix.exists()) {
            throw FileNotFoundException("File ${fileWithSuffix.name} not found")
        }
        return fileWithSuffix
    }

    fun store(bytes: ByteArray, fileName: String): String {
        val fileName = ensureConsistentJPGSuffix(ensureSuffix(fileName, null))

        write(bytes.inputStream(), fileName)

        return fileName
    }

    fun store(file: MultipartFile): String {
        val fileName = ensureConsistentJPGSuffix(ensureSuffix(file.originalFilename!!, file.contentType))

        write(file.inputStream, fileName)

        return fileName
    }

    private fun write(inputStream: InputStream, fileName: String) {
        val targetLocation: Path = Path.of(uploadDir).resolve(fileName)
        Files.createDirectories(targetLocation.parent)
        Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)

        logger.info("Stored ${targetLocation.toAbsolutePath()}")
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