package com.example.duckit.presentation.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class PostUiModel(
    val id: String = "",
    val headline: String = "",
    val image: String = "",
    val upvotes: Int = 0,
    val author: String = "",
): Parcelable
