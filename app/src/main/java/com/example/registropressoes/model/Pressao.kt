package com.example.registropressoes.model

import com.example.registropressoes.extensions.parseToLocaleDateTime
import com.example.registropressoes.extensions.toStringDateTimeBR
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Pressao(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val maxima: Double,
    val minima: Double,
    val data: Long = Clock.System.now().toEpochMilliseconds()
) : Parcelable {
    val dataToBr: String get() = data.parseToLocaleDateTime().toStringDateTimeBR()
}