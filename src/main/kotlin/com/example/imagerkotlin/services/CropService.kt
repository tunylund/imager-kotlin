package com.example.imagerkotlin.services

import com.example.imagerkotlin.controllers.FileStorageService
import com.example.imagerkotlin.controllers.ImagesController
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.format.Format
import com.sksamuel.scrimage.format.FormatDetector
import com.sksamuel.scrimage.nio.GifWriter
import com.sksamuel.scrimage.nio.JpegWriter
import com.sksamuel.scrimage.nio.PngWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.file.Files

@Service
class CropService {

    @Autowired
    private lateinit var fileStorageService: FileStorageService

    suspend fun cropImage(fileName: String, cropParams: ImagesController.CropParams) {
        val file = fileStorageService.get(fileName)
        val targetFileName = "${file.nameWithoutExtension}-${cropParams}.${file.extension}"

        val image = ImmutableImage.loader().fromFile(file)
        val croppedImage = image.subimage(cropParams.x, cropParams.y, cropParams.width, cropParams.height)

        if (fileStorageService.get(targetFileName).exists()) return

        val format = FormatDetector.detect(file.inputStream()).get()
        val (writer, contentType) = when (format) {
            Format.PNG -> Pair(PngWriter.NoCompression, "image/png")
            Format.JPEG -> Pair(JpegWriter.Default, "image/jpg")
            Format.GIF -> Pair(GifWriter.Default, "image/gif")
            Format.WEBP -> Pair(JpegWriter.Default, "image/webp")
            else -> throw IllegalArgumentException("Unsupported file type: ${file.extension}")
        }

        val bytes = croppedImage.bytes(writer)
        fileStorageService.store(bytes, targetFileName, contentType)
    }
}