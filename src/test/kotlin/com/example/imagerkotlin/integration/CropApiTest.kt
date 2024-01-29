package com.example.imagerkotlin.integration

import com.example.imagerkotlin.controllers.ImagesController
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.ClassPathResource
import org.springframework.http.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CropApiTest(@Autowired val restTemplate: TestRestTemplate) {

    @BeforeEach
    fun setup() {
        val file = ClassPathResource("fox.png").file
        Files.copy(Path.of(file.path), Path.of("/tmp/imager-kotlin/fox.png"), StandardCopyOption.REPLACE_EXISTING)
    }

    @Test
    fun `PUT should return 202 for accepted crop query`() {
        val params = ImagesController.CropParams(10, 10, 100, 100)
        val response = restTemplate.exchange("/images/fox.png", HttpMethod.PUT, HttpEntity(params), String::class.java)
        assertEquals(HttpStatus.ACCEPTED, response.statusCode)
    }

    @Test
    fun `PUT should return 404 for image that does not exist`() {
        val params = ImagesController.CropParams(10, 10, 100, 100)
        val response = restTemplate.exchange("/images/does-not-exist.png", HttpMethod.PUT, HttpEntity(params), String::class.java)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `PUT should return 400 for invalid crop query`() {
        val params = ImagesController.CropParams(-10, -10, 100, 100)
        val response = restTemplate.exchange("/images/fox.png", HttpMethod.PUT, HttpEntity(params), String::class.java)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }
}
