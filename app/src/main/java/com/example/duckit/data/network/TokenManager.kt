package com.example.duckit.data.network

import android.content.Context
import android.content.SharedPreferences
import com.example.duckit.presentation.util.KEY_TOKEN
import com.example.duckit.presentation.util.PREF_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveToken(token: String) {
        editor.putString(KEY_TOKEN, token)
        editor.apply()
    }

    val getToken: String?
        get() = sharedPreferences.getString(KEY_TOKEN, null)

    val isTokenSaved: Boolean
        get() = sharedPreferences.contains(KEY_TOKEN)

    fun removeToken() {
        sharedPreferences.edit().remove(KEY_TOKEN).apply()
    }
}
