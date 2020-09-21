package com.alexlepadatu.trendingrepos.data.repository.trending

import android.util.Log
import com.alexlepadatu.inggithub.data.RepoItemDto
import com.alexlepadatu.trendingrepos.data.api.GitApi
import com.alexlepadatu.trendingrepos.data.database.dao.RepositoryItemDao
import com.alexlepadatu.trendingrepos.data.mappers.mapToDomain
import com.alexlepadatu.trendingrepos.data.mappers.mapToEntity
import com.alexlepadatu.trendingrepos.data.models.entity.RepositoryEntity
import com.alexlepadatu.trendingrepos.data.repository.BaseRepositoryImpl
import com.alexlepadatu.trendingrepos.data.utils.DateExt
import com.alexlepadatu.trendingrepos.domain.models.TrendingRepo
import com.alexlepadatu.trendingrepos.domain.repository.TrendingReposRepository
import io.reactivex.Flowable
import com.alexlepadatu.trendingrepos.domain.common.ResultState

class TrendingReposRepositoryImpl (private val api: GitApi,
                                   private val dao: RepositoryItemDao
): BaseRepositoryImpl<List<RepoItemDto>, List<RepositoryEntity>, List<TrendingRepo>>(), TrendingReposRepository {
    override fun getTrendingRepositories(): Flowable<ResultState<List<TrendingRepo>>> {
        val timeToCheck: Long = DateExt.timelessDate().time

        val apiCall = api.fetchRepos()
        val mapperToEntity: (List<RepoItemDto>) -> List<RepositoryEntity> =
            { it.mapToEntity(timeToCheck) }

        val dbFlowable = dao.fetchRepository(timeToCheck)
        val mapperToDomain: (List<RepositoryEntity>) -> List<TrendingRepo> = { it.mapToDomain() }

        val persist: (items: List<RepositoryEntity>) -> Unit = {
            Log.e("TrendingReposRepository", "persist data: ${it.size}")
            dao.insertAll(*it.toTypedArray())
        }

        val shouldFetch: (List<RepositoryEntity>) -> Boolean = {
            it.isNullOrEmpty()
        }

        val shouldReturnErrorCallback: (List<TrendingRepo>) -> Boolean = {
            it.isNullOrEmpty()
        }

        return performGet(
            apiCall,
            mapperToEntity,
            dbFlowable,
            mapperToDomain,
            false,
            shouldFetch,
            shouldReturnErrorCallback,
            persist
        )
    }
}
