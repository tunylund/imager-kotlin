package com.example.imagerkotlin.controllers

import com.example.imagerkotlin.services.CropService
import com.example.imagerkotlin.services.FileStorageService
import com.example.imagerkotlin.services.ResizeService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.constraints.Min
import org.hibernate.validator.constraints.URL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.annotation.RequestScope
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Files
import java.nio.file.Path

@RestController
class ImagesController {

    @Autowired
    lateinit var resizeService: ResizeService

    @Autowired
    lateinit var fileStorageService: FileStorageService

    @Autowired
    lateinit var cropService: CropService


    @GetMapping("/images/{filename}", params = ["!resizeParams"])
    fun getImage(@PathVariable("filename") filename: String): ResponseEntity<ByteArray> {
        try {
            val file = fileStorageService.get(filename)
            return ResponseEntity.ok()
                .header("Content-Type", Files.probeContentType(file.toPath()))
                .body(file.readBytes())
        } catch (e: FileNotFoundException) {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/images/{filename}", params = ["resizeParams"])
    fun getResizedImage(@PathVariable("filename") filename: String, request: HttpServletRequest, @RequestParam(required = false) resizeParams: String): ResponseEntity<ByteArray> {
        val parsedResizeParams = ResizeService.ResizeParams.fromString(resizeParams)
        try {
            val file = fileStorageService.get(filename, parsedResizeParams)
            return ResponseEntity.ok()
                .header("Content-Type", Files.probeContentType(file.toPath()))
                .body(file.readBytes())
        } catch (e: FileNotFoundException) {
            try {
                resizeService.enqueueResizeImage(filename, parsedResizeParams)
                return ResponseEntity.accepted().build()
            } catch (e: FileNotFoundException) {
                return ResponseEntity.notFound().build()
            }
        }
    }

    @PostMapping("/images", headers = ["content-type=multipart/form-data"])
    fun uploadImage(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        val allowedContentTypes = listOf("image/jpeg", "image/png", "image/gif")

        if (file.contentType !in allowedContentTypes) {
            return ResponseEntity("Unsupported file type. Please upload a JPEG, PNG, or GIF image.", HttpStatus.BAD_REQUEST)
        }

        fileStorageService.store(file)

        return ResponseEntity("Hello", HttpStatus.OK)
    }

    @PutMapping("/images/{filename}")
    fun cropImage(@PathVariable("filename") filename: String, @RequestBody @Validated cropParams: CropParams): ResponseEntity<String> {
        try {
            cropService.enqueueCropImage(filename, cropParams)
            return ResponseEntity.accepted().build()
        } catch (e: FileNotFoundException) {
            return ResponseEntity.notFound().build()
        }
    }

    data class CropParams(
        @field:Min(0) val x: Int,
        @field:Min(0) val y: Int,
        @field:Min(0) val width: Int,
        @field:Min(0) val height: Int) {
        override fun toString(): String {
            return "x${x}y${y}-w${width}h${height}"
        }
    }
}