package com.example.duckit.data.repository

import com.example.duckit.common.network.Resource
import com.example.duckit.data.source.DuckitApi
import com.example.duckit.data.source.model.mapper.ErrorCodeMapper
import com.example.duckit.data.source.model.mapper.toDomain
import com.example.duckit.domain.model.Credentials
import com.example.duckit.domain.model.NewPost
import com.example.duckit.domain.model.Post
import com.example.duckit.domain.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.awaitResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val api: DuckitApi
) : PostRepository {

    override suspend fun getPosts(): Flow<Resource<List<Post>>> =
        flow {
            emit(Resource.Loading())

            val result = api.getPosts().posts
            emit(Resource.Success(data = result.map { it.toDomain() }))
        }.catch { exception ->
            emit(Resource.Error(message = exception.message ?: "Unable to get ducks."))
        }.flowOn(Dispatchers.IO)

    override suspend fun signIn(credentials: Credentials): Resource<String> {
        return try {
            val response = api.signIn(credentials).awaitResponse()
            if (response.isSuccessful) {
                Resource.Success(data = response.body()?.token)
            } else {
                Resource.Error(message = ErrorCodeMapper.toMessage(response.code().toString()))
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Having trouble signing in.")
        }
    }

    override suspend fun signUp(credentials: Credentials): Resource<String> {
        return try {
            val response = api.signUp(credentials).awaitResponse()
            if (response.isSuccessful) {
                Resource.Success(data = response.body()?.token)
            } else {
                Resource.Error(message = ErrorCodeMapper.toMessage(response.code().toString()))
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Having trouble signing up.")
        }
    }

    override suspend fun upvote(postId: String): Resource<Int> {
        return try {
            val response = api.upvote(postId).awaitResponse()
            if (response.isSuccessful) {
                Resource.Success(data = response.body()?.upvotes)
            } else {
                Resource.Error(message = response.message() ?: "Couldn't add upvote.")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Having trouble voting.")
        }
    }

    override suspend fun downvote(postId: String): Resource<Int> {
        return try {
            val response = api.downvote(postId).awaitResponse()
            if (response.isSuccessful) {
                Resource.Success(data = response.body()?.upvotes)
            } else {
                Resource.Error(message = response.message() ?: "Couldn't add downvote.")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Having trouble voting.")
        }
    }

    override suspend fun createPost(newPost: NewPost): Resource<Unit> {
        return try {
            val response = api.createPost(newPost).awaitResponse()
            if (response.isSuccessful) {
                Resource.Success(data = Unit)
            } else {
                Resource.Error(message = "Couldn't create new post.")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Having trouble creating new post.")
        }
    }
}
