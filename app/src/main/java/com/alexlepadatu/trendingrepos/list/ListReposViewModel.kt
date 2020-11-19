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
    val trendingRepos: LiveData<ResultState<List<TrendingRepo>>> =
        MutableLiveData<ResultState<List<TrendingRepo>>>().also { mutable ->
        val disposable = trendingReposUseCase.getTrendingRepositories()
            .subscribe {
                mutable.value = it
            }
        addDisposable(disposable)
    }

    init {
        val disposable = trendingReposUseCase.getTrendingRepositories()
            .subscribe {
                backingTrendingRepos.value = it
            }
        addDisposable(disposable)
    }
}