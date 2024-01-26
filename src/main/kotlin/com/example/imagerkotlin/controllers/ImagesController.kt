package com.example.imagerkotlin.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class ImagesController {

    @Autowired
    lateinit var fileStorageService: FileStorageService

    @GetMapping("/images")
    fun getImage(): String {
        return "Hello"
    }

    @RequestMapping(path=["/images"], method = [RequestMethod.POST], headers = ["content-type=multipart/form-data"])
    fun uploadImage(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        val allowedContentTypes = listOf("image/jpeg", "image/png", "image/gif")

        if (file.contentType !in allowedContentTypes) {
            return ResponseEntity("Unsupported file type. Please upload a JPEG, PNG, or GIF image.", HttpStatus.BAD_REQUEST)
        }

        fileStorageService.store(file)

        return ResponseEntity("Hello", HttpStatus.OK)
    }

}