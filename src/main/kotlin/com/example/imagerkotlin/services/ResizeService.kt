package com.example.imagerkotlin.services

import com.example.imagerkotlin.controllers.ImagesController
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.core.MatOfRect
import org.opencv.core.Size
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.io.FileNotFoundException


@Service
class ResizeService {

    @Autowired
    private lateinit var fileStorageService: FileStorageService

    @Autowired
    private lateinit var resourceLoader: ResourceLoader

    private val faceCascade: CascadeClassifier by lazy {
        val resource = resourceLoader.getResource("classpath:haarcascade_frontalface_default.xml")
        CascadeClassifier(resource.file.absolutePath)
    }

    fun resizeImage(fileName: String, resizeParams: ImagesController.ResizeParams) {
        val file = fileStorageService.get(fileName)
        val srcImage = Imgcodecs.imread(file.absolutePath)
        if (srcImage.empty()) throw FileNotFoundException(file.toString())

        val destSize = Size(resizeParams.width.toDouble(), resizeParams.height.toDouble())
        val destImage = Mat(destSize, srcImage.type())
        val destFileName = "${file.nameWithoutExtension}-${resizeParams}.${file.extension}"

        val faces = MatOfRect()
        faceCascade.detectMultiScale(srcImage, faces)
        val faceDetections = faces.toArray()

        if (resizeParams.centerOnFace and faceDetections.isNotEmpty()) {
            val face = faceDetections[0]
            val faceMat = Mat(srcImage, face)
            Imgproc.resize(faceMat, destImage, destSize)
        } else {
            Imgproc.resize(srcImage, destImage, destSize)
        }

        val bytes = MatOfByte()
        Imgcodecs.imencode(".${file.extension}", destImage, bytes)

        fileStorageService.store(bytes.toArray(), destFileName)
    }

    @Async
    fun enqueueResizeImage(fileName: String, resizeParams: ImagesController.ResizeParams) {
        fileStorageService.get(fileName)
        resizeImage(fileName, resizeParams)
    }
}