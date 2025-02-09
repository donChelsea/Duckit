package com.example.duckit.data.repository

import com.example.duckit.common.Resource
import com.example.duckit.data.source.DuckitApi
import com.example.duckit.data.source.model.mapper.toDomain
import com.example.duckit.domain.model.Post
import com.example.duckit.domain.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val api: DuckitApi
): PostRepository {

    override suspend fun getPost(): Flow<Resource<List<Post>>> =
        flow {
            emit(Resource.Loading())

            val result = api.getPosts().posts
            emit(Resource.Success(data = result.map { it.toDomain() }))
        }.catch { exception ->
            emit(Resource.Error(message = exception.message ?: "Unable to get ducks."))
        }.flowOn(Dispatchers.IO)
}
