package com.example.duckit.data.source

import com.example.duckit.data.source.model.PostDto
import com.example.duckit.data.source.model.PostsListDto
import com.example.duckit.data.source.model.TokenDto
import com.example.duckit.data.source.model.UpvoteDto
import com.example.duckit.domain.model.NewPost
import com.example.duckit.domain.model.Credentials
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface DuckitApi {

    @GET("posts")
    suspend fun getPosts(): PostsListDto<PostDto>

    @POST("signup")
    fun signUp(@Body credentials: Credentials): Call<TokenDto>

    @POST("signin")
    fun signIn(@Body credentials: Credentials): Call<TokenDto>

    @POST("posts/{id}/upvote")
    fun upvote(@Path("id") postId: String): Call<UpvoteDto>

    @POST("posts/{id}/downvote")
    fun downvote(@Path("id") postId: String): Call<UpvoteDto>

    @POST("posts")
    fun createPost(@Body newPost: NewPost): Call<Unit>
}
