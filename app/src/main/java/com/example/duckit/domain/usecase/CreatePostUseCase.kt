package com.example.duckit.domain.usecase

import com.example.duckit.domain.model.NewPost
import com.example.duckit.domain.repository.PostRepository
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(
        newPost: NewPost
    ) = repository.createPost(newPost)
}