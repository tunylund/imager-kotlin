package com.example.imagerkotlin

import nu.pattern.OpenCV
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class ImagerKotlinApplication

fun main(args: Array<String>) {
    OpenCV.loadShared()
    runApplication<ImagerKotlinApplication>(*args)
}
