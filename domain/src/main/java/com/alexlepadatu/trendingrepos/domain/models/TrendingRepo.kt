package com.alexlepadatu.trendingrepos.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrendingRepo(
    val name: String,
    val description: String,
    val language: String,
    val avatar: String,
    val url: String,
    val currentPeriodStars: Int,
    val forks: Int,
    val stars: Int,
    val builtBy: List<TrendingRepoAuthor>
): Parcelable

@Parcelize
data class TrendingRepoAuthor(
    val username: String,
    val href: String,
    val avatar: String
): Parcelable