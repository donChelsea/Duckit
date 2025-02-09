package com.example.duckit.data.source.model

import com.google.gson.annotations.SerializedName

data class UpvoteDto(
    @SerializedName("upvotes")
    val upvotes: Int
)