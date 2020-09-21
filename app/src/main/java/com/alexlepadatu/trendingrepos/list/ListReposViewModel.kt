package com.alexlepadatu.trendingrepos.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.alexlepadatu.trendingrepos.base.BaseViewModel
import com.alexlepadatu.trendingrepos.domain.models.TrendingRepo
import com.alexlepadatu.trendingrepos.domain.useCase.TrendingReposUseCase
import com.alexlepadatu.trendingrepos.domain.common.ResultState

class ListReposViewModel (trendingReposUseCase : TrendingReposUseCase): BaseViewModel() {

    private val backingTrendingRepos: MutableLiveData<ResultState<List<TrendingRepo>>> = MutableLiveData()
    val trendingRepos: LiveData<ResultState<List<TrendingRepo>>> = Transformations.switchMap(backingTrendingRepos) {
        MutableLiveData(it)
    }

    init {
        val disposable = trendingReposUseCase.getTrendingRepositories().subscribe {
            backingTrendingRepos.value = it
        }
        addDisposable(disposable)
    }
}