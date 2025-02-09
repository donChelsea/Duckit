package com.example.duckit.data.source

import com.example.duckit.data.source.model.PostDto
import com.example.duckit.data.source.model.PostsListDto
import retrofit2.http.GET

interface DuckitApi {

    @GET("posts")
    suspend fun getPosts(): PostsListDto<PostDto>
}
