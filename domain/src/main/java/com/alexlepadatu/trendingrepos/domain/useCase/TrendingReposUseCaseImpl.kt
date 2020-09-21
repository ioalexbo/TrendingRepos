package com.alexlepadatu.trendingrepos.domain.useCase

import com.alexlepadatu.trendingrepos.domain.common.SchedulerProvider
import com.alexlepadatu.trendingrepos.domain.models.TrendingRepo
import com.alexlepadatu.trendingrepos.domain.repository.TrendingReposRepository
import io.reactivex.Flowable
import com.alexlepadatu.trendingrepos.domain.common.ResultState

class TrendingReposUseCaseImpl (
    private val schedulerProvider: SchedulerProvider,
    private val trendingRepo: TrendingReposRepository): TrendingReposUseCase {

    override fun getTrendingRepositories(): Flowable<ResultState<List<TrendingRepo>>> {
        return trendingRepo.getTrendingRepositories()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}