package com.example.imagerkotlin.services

import com.example.imagerkotlin.prepareTestFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path

@SpringBootTest
@ContextConfiguration(classes = [FileStorageService::class])
@TestPropertySource(properties = ["uploadDir=\${java.io.tmpdir}"])
class FileStorageServiceTest {

    @Autowired
    lateinit var fileStorageService: FileStorageService

    @Value("\${uploadDir}")
    lateinit var uploadDir: String

    @Test
    fun `should return the file if it exists`() {
        prepareTestFile("fox.png", uploadDir)

        val file = fileStorageService.get("fox.png")

        assertTrue(file.exists())
    }

    @Test
    fun `should return the file with suffix if it exists`() {
        val resizeParams = ResizeService.ResizeParams(50, 50, false)
        prepareTestFile("fox.png", "fox-${resizeParams}", uploadDir)

        val file = fileStorageService.get("fox.png", resizeParams)

        assert(file.exists())
    }

    @Test
    fun `should throw FileNotFoundException if the file with suffix does not exist`() {
        val resizeParams = ResizeService.ResizeParams(50, 50, false)
        assertThrows<FileNotFoundException> {
            fileStorageService.get("does-not-exist", resizeParams)
        }
    }

    @Test
    fun `store should correctly store a file and return the file name`() {
        val originalFileName = "test.png"
        val mockMultipartFile = MockMultipartFile(
            "test.png",
            originalFileName,
            "image/png",
            "test data".toByteArray()
        )

        val storedFileName = fileStorageService.store(mockMultipartFile)

        assertEquals(originalFileName, storedFileName)
        assertTrue(Files.exists(Path.of(uploadDir).resolve(originalFileName)))
    }

    @Test
    fun `store should provide a suffix for the filename based on the content type`() {
        val mockMultipartFile = MockMultipartFile(
            "test",
            "test",
            "image/png",
            "test data".toByteArray()
        )

        val storedFileName = fileStorageService.store(mockMultipartFile)

        assertEquals("test.png", storedFileName)
    }

    @Test
    fun `store should make jpeg vs jpg situation consistent`() {
        val mockMultipartFile = MockMultipartFile(
            "test",
            "test",
            "image/jpeg",
            "test data".toByteArray()
        )

        val storedFileName = fileStorageService.store(mockMultipartFile)

        assertEquals("test.jpg", storedFileName)
    }
}