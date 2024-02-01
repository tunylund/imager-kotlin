package com.example.imagerkotlin.services

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
//        CascadeClassifier(ClassPathResource("haarcascade_frontalface_default.xml").file.absolutePath)
    }

    data class ResizeParams(val width: Int, val height: Int, val centerOnFace: Boolean = false) {
        override fun toString(): String {
            return "${width}x${height}f${centerOnFace}"
        }

        companion object {
            fun fromString(resizeParams: String): ResizeParams {
                val params = resizeParams.split("x", "f")
                return ResizeParams(params[0].toInt(), params[1].toInt(), params[2].toBoolean())
            }
        }
    }

    fun resizeImage(fileName: String, resizeParams: ResizeParams) {
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
    fun enqueueResizeImage(fileName: String, resizeParams: ResizeParams) {
        fileStorageService.get(fileName)
        resizeImage(fileName, resizeParams)
    }
}