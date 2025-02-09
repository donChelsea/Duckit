package com.example.duckit.common.network

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    suspend fun saveToken(token: String?) {
        editor.putString(KEY_TOKEN, token)
        editor.apply()
    }

    val getToken: String?
        get() = sharedPreferences.getString(KEY_TOKEN, null)

    val isTokenSaved: Boolean
        get() = sharedPreferences.contains(KEY_TOKEN)

    companion object {
        private const val PREF_NAME = "shared_prefs"
        private const val KEY_TOKEN = "auth_token"
    }
}