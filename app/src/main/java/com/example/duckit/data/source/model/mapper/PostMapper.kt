package com.example.duckit.data.source.model.mapper

import com.example.duckit.data.source.model.PostDto
import com.example.duckit.domain.model.Post

fun PostDto.toDomain() = Post(
    id = id,
    headline = headline,
    image = image,
    upvotes = upvotes,
    author = author,
)
