package com.alexlepadatu.trendingrepos.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.alexlepadatu.trendingrepos.base.BaseViewModel
import com.alexlepadatu.trendingrepos.domain.models.TrendingRepo

class DetailRepoViewModel : BaseViewModel() {

    private val trendingRepoBacking = MutableLiveData<TrendingRepo>()

    val trendingRepo: LiveData<TrendingRepo> = Transformations.switchMap(trendingRepoBacking) { repo ->
        MutableLiveData<TrendingRepo>().also {
            it.value = repo
        }
    }

    fun setTrendingRepo(repo: TrendingRepo?) {
        repo?.let {
            trendingRepoBacking.value = it
        }
    }
}