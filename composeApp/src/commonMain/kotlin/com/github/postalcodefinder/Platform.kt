package com.github.postalcodefinder

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform