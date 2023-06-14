package com.example.savingtime.model

data class EkonomiApiResponse(
    val data: List<News>,
    val length: Int,
    val status: Int
)

data class News(
    val judul: String,
    val link: String,
    val poster: String
)