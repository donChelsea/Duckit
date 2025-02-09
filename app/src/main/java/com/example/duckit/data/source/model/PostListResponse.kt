package com.example.duckit.data.source.model

import com.google.gson.annotations.SerializedName

data class PostsListDto<T>(
    @SerializedName("Posts")
    val posts: List<T>
)