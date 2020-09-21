package com.alexlepadatu.trendingrepos.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexlepadatu.trendingrepos.domain.useCase.TrendingReposUseCase

class ListReposViewModelFactory (private val trendingReposUseCase : TrendingReposUseCase)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ListReposViewModel(trendingReposUseCase) as T
    }
}