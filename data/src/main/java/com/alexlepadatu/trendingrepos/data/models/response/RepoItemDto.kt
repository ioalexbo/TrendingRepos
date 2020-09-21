package com.alexlepadatu.inggithub.data

import com.alexlepadatu.ing.gitrepos.data.model.RepoAuthorDto
import com.google.gson.annotations.SerializedName

data class RepoItemDto(
    val name: String,
    val description: String,
    val language: String,
    val avatar: String,
    val url: String,
    val currentPeriodStars: Int,

    @SerializedName("forks")
    val forksCount: Int,

    @SerializedName("stars")
    val starsCount: Int,

    var builtBy: List<RepoAuthorDto>
)