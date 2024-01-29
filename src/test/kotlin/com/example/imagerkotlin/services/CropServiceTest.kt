package com.example.imagerkotlin.services

import com.example.imagerkotlin.controllers.FileStorageService
import com.example.imagerkotlin.controllers.ImagesController
import com.sksamuel.scrimage.ImmutableImage
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@SpringBootTest
@ContextConfiguration
@TestPropertySource(properties = ["uploadDir=\${java.io.tmpdir}"])
class CropServiceTest {

    @Autowired
    lateinit var cropService: CropService

    @Value("\${uploadDir}")
    lateinit var uploadDir: String

    @Test
    fun `crop should resize the given image file and create a new image`() {
        Files.copy(Path.of(ClassPathResource("fox.png").file.path), Path.of(uploadDir).resolve("fox.png"), StandardCopyOption.REPLACE_EXISTING)

        val cropParams = ImagesController.CropParams(10, 10, 100, 100)
        runBlocking { cropService.cropImage("fox.png", cropParams) }

        ImmutableImage.loader().fromFile("${uploadDir}fox-${cropParams}.png").apply {
            assertEquals(100, width)
            assertEquals(100, height)
        }
    }

    @Test
    fun `should resize jpg as well`() {
        Files.copy(Path.of(ClassPathResource("fox.jpg").file.path), Path.of(uploadDir).resolve("fox.jpg"), StandardCopyOption.REPLACE_EXISTING)

        val cropParams = ImagesController.CropParams(10, 10, 100, 100)
        runBlocking { cropService.cropImage("fox.jpg", cropParams) }

        ImmutableImage.loader().fromFile("${uploadDir}fox-${cropParams}.jpg").apply {
            assertEquals(100, width)
            assertEquals(100, height)
        }
    }

}