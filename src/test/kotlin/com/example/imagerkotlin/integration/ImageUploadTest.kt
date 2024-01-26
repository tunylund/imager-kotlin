package com.example.imagerkotlin.integration

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.ClassPathResource
import org.springframework.http.*
import org.springframework.util.LinkedMultiValueMap
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ImageUploadTest(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    fun `should receive the image if it exists`() {
        val file = ClassPathResource("fox.png").file
        Files.copy(Path.of(file.path), Path.of("/tmp/imager-kotlin/fox.png"), StandardCopyOption.REPLACE_EXISTING)

        restTemplate.getForEntity("/images/fox.png", ByteArray::class.java)
            .apply {
                assertEquals(HttpStatus.OK, statusCode)
                assertEquals(MediaType.IMAGE_PNG, headers.contentType)
                assertNotNull(body)
                assert(body!!.isNotEmpty())
            }
    }

    @Test
    fun `should return 200 for valid image upload`() {
        val response = uploadImage("fox.png")

        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun `should return 400 for an unsupported image type`() {
        val response = uploadImage("fox.svg")

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    private fun uploadImage(file: String): ResponseEntity<String> {
        val headers = HttpHeaders().apply {
            contentType = MediaType.MULTIPART_FORM_DATA
        }

        val resource = ClassPathResource(file)
        val body = LinkedMultiValueMap<String, Any>().apply {
            add("file", resource)
        }

        val requestEntity = HttpEntity(body, headers)
        return restTemplate.exchange("/images", HttpMethod.POST, requestEntity, String::class.java)
    }
}