package com.example.duckit.data.source.model

import com.google.gson.annotations.SerializedName

data class TokenDto(
    @SerializedName("token")
    val token: String,
)
