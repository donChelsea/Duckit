package com.example.duckit.domain.repository

import com.example.duckit.common.Resource
import com.example.duckit.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun getPost(): Flow<Resource<List<Post>>>
}
