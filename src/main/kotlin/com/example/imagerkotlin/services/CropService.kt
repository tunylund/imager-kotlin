package com.example.imagerkotlin.services

import com.example.imagerkotlin.controllers.ImagesController
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.format.Format
import com.sksamuel.scrimage.format.FormatDetector
import com.sksamuel.scrimage.nio.GifWriter
import com.sksamuel.scrimage.nio.JpegWriter
import com.sksamuel.scrimage.nio.PngWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class CropService {

    @Autowired
    private lateinit var fileStorageService: FileStorageService

    fun cropImage(fileName: String, cropParams: ImagesController.CropParams) {
        val file = fileStorageService.get(fileName)
        val targetFileName = "${file.nameWithoutExtension}-${cropParams}.${file.extension}"

        val image = ImmutableImage.loader().fromFile(file)
        val croppedImage = image.subimage(cropParams.x, cropParams.y, cropParams.width, cropParams.height)

        val format = FormatDetector.detect(file.inputStream()).get()
        val writer = when (format) {
            Format.PNG -> PngWriter.NoCompression
            Format.JPEG -> JpegWriter.Default
            Format.GIF -> GifWriter.Default
            Format.WEBP -> JpegWriter.Default
            else -> throw IllegalArgumentException("Unsupported file type: ${file.extension}")
        }

        val bytes = croppedImage.bytes(writer)
        fileStorageService.store(bytes, targetFileName)
    }

    @Async
    fun enqueueCropImage(fileName: String, cropParams: ImagesController.CropParams) {
        val file = fileStorageService.get(fileName)
        if (file.exists()) cropImage(fileName, cropParams)
    }
}