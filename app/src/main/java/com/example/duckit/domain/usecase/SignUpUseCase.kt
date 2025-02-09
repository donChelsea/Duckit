package com.example.duckit.domain.usecase

import com.example.duckit.domain.model.Credentials
import com.example.duckit.domain.repository.PostRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(
        credentials: Credentials
    ) = repository.signUp(credentials)
}