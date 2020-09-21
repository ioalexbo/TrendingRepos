package com.alexlepadatu.trendingrepos.data.api

import com.alexlepadatu.inggithub.data.RepoItemDto
import io.reactivex.Single
import retrofit2.http.GET

interface GitApi {

    @GET("repositories?language=kotlin&since=daily")
    fun fetchRepos(): Single<List<RepoItemDto>>
}