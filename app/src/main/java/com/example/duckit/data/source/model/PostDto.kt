package com.example.duckit.data.source.model

import com.google.gson.annotations.SerializedName

data class PostDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("headline")
    val headline: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("upvotes")
    val upvotes: Int,
    @SerializedName("author")
    val author: String,
)