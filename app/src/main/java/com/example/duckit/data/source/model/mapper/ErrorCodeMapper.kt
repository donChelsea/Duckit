package com.example.duckit.data.source.model.mapper

object ErrorCodeMapper {
    fun toMessage(code: String): String =
        when (code) {
            "403" -> "Password is incorrect."
            "404" -> "Account can't be found."
            "409" -> "Account already exists."
            else -> "Unknown error."
        }
}
