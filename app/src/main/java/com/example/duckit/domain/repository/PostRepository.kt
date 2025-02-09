package com.example.duckit.domain.repository

import com.example.duckit.common.Resource
import com.example.duckit.domain.model.NewPost
import com.example.duckit.domain.model.Credentials
import com.example.duckit.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun getPosts(): Flow<Resource<List<Post>>>
    suspend fun signIn(credentials: Credentials): Resource<String>
    suspend fun signUp(credentials: Credentials): Resource<String>
    suspend fun upvote(postId: String): Resource<Int>
    suspend fun downvote(postId: String): Resource<Int>
    suspend fun createPost(newPost: NewPost): Resource<Unit>
}
