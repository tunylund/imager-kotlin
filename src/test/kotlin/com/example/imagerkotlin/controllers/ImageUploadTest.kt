package com.example.imagerkotlin.controllers

import com.example.imagerkotlin.services.FileStorageService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.ClassPathResource
import org.springframework.http.*
import org.springframework.util.LinkedMultiValueMap

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ImageUploadTest(@Autowired val restTemplate: TestRestTemplate) {

    @MockkBean
    private lateinit var fileStorageService: FileStorageService

    @Test
    fun `POST should return 200 for valid image upload`() {
        every { fileStorageService.store(any()) } returns "some-image.jpg"

        val response = uploadImage("fox.png")

        assertEquals(HttpStatus.OK, response.statusCode)
        verify { fileStorageService.store(any()) }
    }

    @Test
    fun `POST should return 400 for an unsupported image type`() {
        val response = uploadImage("fox.svg")

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    private fun uploadImage(file: String): ResponseEntity<String> {
        val headers = HttpHeaders().apply {
            contentType = MediaType.MULTIPART_FORM_DATA
        }

        val body = LinkedMultiValueMap<String, Any>().apply {
            add("file", ClassPathResource(file))
        }

        return restTemplate.exchange("/images", HttpMethod.POST, HttpEntity(body, headers), String::class.java)
    }
}