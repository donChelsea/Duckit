package com.example.duckit.presentation.model.mapper

import com.example.duckit.domain.model.Post
import com.example.duckit.presentation.model.PostUiModel

fun Post.toUiModel() = PostUiModel(
    id = id,
    headline = headline,
    image = image,
    upvotes = upvotes,
    author = author,
)
