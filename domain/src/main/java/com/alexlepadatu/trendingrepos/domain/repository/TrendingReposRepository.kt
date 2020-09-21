package com.alexlepadatu.trendingrepos.domain.repository

import com.alexlepadatu.trendingrepos.domain.models.TrendingRepo
import io.reactivex.Flowable
import com.alexlepadatu.trendingrepos.domain.common.ResultState

interface TrendingReposRepository {

    fun getTrendingRepositories(): Flowable<ResultState<List<TrendingRepo>>>
}