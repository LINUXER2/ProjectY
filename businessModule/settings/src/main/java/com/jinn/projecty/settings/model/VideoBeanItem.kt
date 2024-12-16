package com.jinn.projecty.settings.model


import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class VideoBeanItem(
    val authorName: String = "",
    val collectionCount: String = "",
    val desc: String = "",
    val id: Int = 0,
    val imageUrl: String = "",
    val playUrl: String = "",
    val title: String = ""
) : Parcelable