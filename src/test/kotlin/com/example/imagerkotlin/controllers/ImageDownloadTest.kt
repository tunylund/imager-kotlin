package com.example.imagerkotlin.controllers

import com.example.imagerkotlin.services.FileStorageService
import com.example.imagerkotlin.services.ResizeService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.io.FileNotFoundException

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ImageDownloadTest(@Autowired val restTemplate: TestRestTemplate) {

    @MockkBean
    private lateinit var fileStorageService: FileStorageService

    @MockkBean
    private lateinit var resizeService: ResizeService

    @Test
    fun `GET should return the image when it exists`() {
        every { fileStorageService.get("some-image.png") } returns ClassPathResource("fox.png").file

        restTemplate.getForEntity("/images/some-image.png", ByteArray::class.java)
            .apply {
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(MediaType.IMAGE_PNG, headers.contentType)
                Assertions.assertNotNull(body)
                assert(body!!.isNotEmpty())
            }
    }

    @Test
    fun `GET should return the resized image when it already exists`() {
        val params = ResizeService.ResizeParams(50, 50, false)
        every { fileStorageService.get("some-image.png", params) } returns ClassPathResource("fox.png").file

        restTemplate.getForEntity("/images/some-image.png?resizeParams={resizeParams}", ByteArray::class.java, params)
            .apply {
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(MediaType.IMAGE_PNG, headers.contentType)
                Assertions.assertNotNull(body)
                assert(body!!.isNotEmpty())
            }
    }

    @Test
    fun `GET should return accepted and enqueue resize operation if the resized image does not exist`() {
        val params = ResizeService.ResizeParams(50, 50, false)
        every { fileStorageService.get("some-image.png", params) } throws FileNotFoundException()
        every { resizeService.enqueueResizeImage("some-image.png", params) } returns Unit

        val response = restTemplate.getForEntity("/images/some-image.png?resizeParams={resizeParams}", ByteArray::class.java, params)
        assertEquals(HttpStatus.ACCEPTED, response.statusCode)
        verify { resizeService.enqueueResizeImage("some-image.png", params) }
    }

    @Test
    fun `GET should return 404 if the original image does not exist for resizing`() {
        val params = ResizeService.ResizeParams(50, 50, false)
        every { fileStorageService.get("some-image.png", params) } throws FileNotFoundException()
        every { resizeService.enqueueResizeImage("some-image.png", params) } throws FileNotFoundException()

        val response = restTemplate.getForEntity("/images/some-image.png?resizeParams={resizeParams}", ByteArray::class.java, params)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `GET should return 404 if the image does not exist`() {
        every { fileStorageService.get("does-not-exist.png") } throws FileNotFoundException()

        val response = restTemplate.getForEntity("/images/does-not-exist.png", ByteArray::class.java)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }
}