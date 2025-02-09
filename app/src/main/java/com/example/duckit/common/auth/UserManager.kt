package com.example.duckit.common.auth

import android.content.Context
import android.content.SharedPreferences
import com.example.duckit.data.network.TokenManager
import com.example.duckit.domain.model.Credentials
import com.example.duckit.domain.model.User
import com.example.duckit.presentation.util.PREF_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    @ApplicationContext context: Context,
    private val tokenManager: TokenManager,
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    private val KEY_EMAIL = "email"
    private val KEY_PASSWORD = "password"

    val isSignedIn: Boolean
        get() = tokenManager.isTokenSaved

    fun signIn(credentials: Credentials, token: String) {
        editor.putString(KEY_EMAIL, credentials.email).apply()
        editor.putString(KEY_PASSWORD, credentials.password).apply()
        tokenManager.saveToken(token)
    }

    fun getUser(): User {
        val email = sharedPreferences.getString(KEY_EMAIL, null)
        val password = sharedPreferences.getString(KEY_PASSWORD, null)
        val token = tokenManager.getToken.orEmpty()
        return User(
            email = email.orEmpty(),
            password = password.orEmpty(),
            token = token
        )
    }

    fun signOut() {
        editor.remove(KEY_EMAIL).apply()
        editor.remove(KEY_PASSWORD).apply()
        tokenManager.removeToken()
    }
}