package com.example.imagerkotlin.controllers

import com.example.imagerkotlin.services.CropService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.io.FileNotFoundException

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CropApiTest(@Autowired val restTemplate: TestRestTemplate) {

    @MockkBean
    lateinit var cropService: CropService

    @Test
    fun `PUT should return 202 for accepted crop query`() {
        val params = ImagesController.CropParams(10, 10, 100, 100)
        every { cropService.enqueueCropImage("some-image.png", params) } returns Unit

        val response = restTemplate.exchange("/images/some-image.png", HttpMethod.PUT, HttpEntity(params), String::class.java)

        assertEquals(HttpStatus.ACCEPTED, response.statusCode)
        cropService.enqueueCropImage("some-image.png", params)
    }

    @Test
    fun `PUT should return 404 for image that does not exist`() {
        val params = ImagesController.CropParams(10, 10, 100, 100)
        every { cropService.enqueueCropImage("does-not-exist.png", params) } throws FileNotFoundException()

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
