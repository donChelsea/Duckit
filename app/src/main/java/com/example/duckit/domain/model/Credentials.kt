package com.example.duckit.domain.model

data class Credentials(
    var email: String = "",
    var password: String = "",
) {
    fun isNotEmpty(): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }
}