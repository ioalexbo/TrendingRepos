package com.alexlepadatu.trendingrepos.domain.useCase

import com.alexlepadatu.trendingrepos.domain.models.TrendingRepo
import io.reactivex.Flowable
import com.alexlepadatu.trendingrepos.domain.common.ResultState

interface TrendingReposUseCase {
    fun getTrendingRepositories(): Flowable<ResultState<List<TrendingRepo>>>
}