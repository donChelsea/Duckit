package com.example.duckit.data.network

import com.example.duckit.domain.network.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        val token = runBlocking {
            tokenManager.getToken.orEmpty()
        }
        return runBlocking {
            response.request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        }
    }
}