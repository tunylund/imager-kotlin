package com.example.imagerkotlin.services

import com.example.imagerkotlin.prepareTestFile
import com.sksamuel.scrimage.ImmutableImage
import nu.pattern.OpenCV
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import java.io.FileNotFoundException

@SpringBootTest
@ContextConfiguration(classes = [ResizeService::class, FileStorageService::class])
@TestPropertySource(properties = ["uploadDir=\${java.io.tmpdir}"])
class ResizeServiceTest {

    @Value("\${uploadDir}")
    lateinit var uploadDir: String

    @Autowired
    lateinit var resizeService: ResizeService

    companion object {
        @JvmStatic
        @BeforeAll
        fun setup(): Unit {
            OpenCV.loadLocally()
        }
    }

    @Test
    fun `should raise an exception if the file is not found`() {
        assertThrows(FileNotFoundException::class.java) {
            resizeService.resizeImage("not-found.png", ResizeService.ResizeParams(50, 50))
        }
    }

    @Test
    fun `enqueueResize should raise an exception if the file is not found`() {
        assertThrows(FileNotFoundException::class.java) {
            resizeService.enqueueResizeImage("not-found.png", ResizeService.ResizeParams(50, 50))
        }
    }

    @Test
    fun `should resize the given image file and create a new image`() {
        prepareTestFile("fox.png", uploadDir)

        val resizeParams = ResizeService.ResizeParams(50, 50)
        resizeService.resizeImage("fox.png", resizeParams)

        ImmutableImage.loader().fromFile("${uploadDir}fox-${resizeParams}.png").apply {
            assertEquals(50, width)
            assertEquals(50, height)
        }
    }
    @Test
    fun `should resize the given image file and center on face`() {
        prepareTestFile("face1-orig.jpg", uploadDir)

        val resizeParams = ResizeService.ResizeParams(50, 50, true)
        resizeService.resizeImage("face1-orig.jpg", resizeParams)

        val expectedResultImage = ImmutableImage.loader().fromFile(ClassPathResource("face1-result.jpg").file.path)
        val actualResultImage = ImmutableImage.loader().fromFile("${uploadDir}face1-orig-${resizeParams}.jpg")
        assertArrayEquals(expectedResultImage.argbints(), actualResultImage.argbints())
    }

    @Test
    fun `should resize the given image file even if it has no faces`() {
        prepareTestFile("fox.png", uploadDir)

        val resizeParams = ResizeService.ResizeParams(50, 50, true)
        resizeService.resizeImage("fox.png", resizeParams)

        ImmutableImage.loader().fromFile("${uploadDir}fox-${resizeParams}.png").apply {
            assertEquals(50, width)
            assertEquals(50, height)
        }
    }
}
