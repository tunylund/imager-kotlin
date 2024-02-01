package com.example.imagerkotlin.services

import com.example.imagerkotlin.controllers.ImagesController
import com.example.imagerkotlin.prepareTestFile
import com.sksamuel.scrimage.ImmutableImage
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import java.io.FileNotFoundException

@SpringBootTest
@ContextConfiguration(classes = [CropService::class, FileStorageService::class])
@TestPropertySource(properties = ["uploadDir=\${java.io.tmpdir}"])
class CropServiceTest {

    @Autowired
    lateinit var cropService: CropService

    @Value("\${uploadDir}")
    lateinit var uploadDir: String

    @Test
    fun `crop should resize the given image file and create a new image`() {
        prepareTestFile("fox.png", uploadDir)

        val cropParams = ImagesController.CropParams(10, 10, 100, 100)
        cropService.cropImage("fox.png", cropParams)

        ImmutableImage.loader().fromFile("${uploadDir}fox-${cropParams}.png").apply {
            assertEquals(100, width)
            assertEquals(100, height)
        }
    }

    @Test
    fun `should resize jpg as well`() {
        prepareTestFile("fox.png", uploadDir)

        val cropParams = ImagesController.CropParams(10, 10, 100, 100)
        cropService.cropImage("fox.jpg", cropParams)

        ImmutableImage.loader().fromFile("${uploadDir}fox-${cropParams}.jpg").apply {
            assertEquals(100, width)
            assertEquals(100, height)
        }
    }

    @Test
    fun `should throw an exception if the original file does not exist`() {
        val cropParams = ImagesController.CropParams(10, 10, 100, 100)

        assertThrows<FileNotFoundException>() {
            cropService.cropImage("does-not-exist.png", cropParams)
        }
    }

    @Test
    fun `should overwrite the file if it already exists`() {
        val cropParams = ImagesController.CropParams(10, 10, 100, 100)
        prepareTestFile("fox.png", uploadDir)
        prepareTestFile("fox.png", "fox-${cropParams}.png", uploadDir)

        cropService.cropImage("fox.png", cropParams)
    }

    @Test
    fun `enqueue crop image should throw if the original file does not exist` () {
        val cropParams = ImagesController.CropParams(10, 10, 100, 100)

        assertThrows<FileNotFoundException>() {
            cropService.enqueueCropImage("does-not-exist.png", cropParams)
        }
    }

}