package com.alexlepadatu.trendingrepos.data.models.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.alexlepadatu.trendingrepos.data.database.ComplexTypeConverters
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "repository_items",
    indices = [Index(value = ["name"], unique = true)])
//@TypeConverters(ComplexTypeConverters::class)
@Parcelize
data class RepositoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val language: String,
    val avatar: String,
    val url: String,
    val currentPeriodStars: Int,

    val forksCount: Int,

    val starsCount: Int,

    val builtBy: List<RepositoryAuthor>,

    val fetchedAt: Long
): Parcelable


@Parcelize
data class RepositoryAuthor(
    val username: String,
    val href: String,
    val avatar: String): Parcelable