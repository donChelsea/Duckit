package com.example.duckit.domain.usecase

import com.example.duckit.domain.repository.PostRepository
import javax.inject.Inject

class UpvoteUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(
        postId: String
    ) = repository.upvote(postId)
}