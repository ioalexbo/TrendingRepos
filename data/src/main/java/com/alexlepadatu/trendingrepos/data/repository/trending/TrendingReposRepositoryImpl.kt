package com.alexlepadatu.trendingrepos.data.repository.trending

import com.alexlepadatu.inggithub.data.RepoItemDto
import com.alexlepadatu.trendingrepos.data.api.GitApi
import com.alexlepadatu.trendingrepos.data.database.dao.RepositoryItemDao
import com.alexlepadatu.trendingrepos.data.mappers.mapToDomain
import com.alexlepadatu.trendingrepos.data.mappers.mapToEntity
import com.alexlepadatu.trendingrepos.data.models.entity.RepositoryEntity
import com.alexlepadatu.trendingrepos.data.repository.NetworkBoundResource
import com.alexlepadatu.trendingrepos.data.utils.DateExt
import com.alexlepadatu.trendingrepos.domain.common.ResultState
import com.alexlepadatu.trendingrepos.domain.models.TrendingRepo
import com.alexlepadatu.trendingrepos.domain.repository.TrendingReposRepository
import io.reactivex.Flowable
import io.reactivex.Single

class TrendingReposRepositoryImpl (private val api: GitApi,
                                   private val dao: RepositoryItemDao
): TrendingReposRepository {
    override fun getTrendingRepositories(): Flowable<ResultState<List<TrendingRepo>>> {
        val timeToCheck: Long = DateExt.timelessDate().time

        return object: NetworkBoundResource<List<RepoItemDto>, List<RepositoryEntity>, List<TrendingRepo>>() {

            override fun persist(entity: List<RepositoryEntity>) {
                dao.insertAll(*entity.toTypedArray())
            }

            override fun shouldReturnErrorCallback(domain: List<TrendingRepo>): Boolean {
                return domain.isNullOrEmpty()
            }

            override fun shouldFetch(entity: List<RepositoryEntity>): Boolean {
                return entity.isNullOrEmpty()
            }

            override fun alwaysRefresh(): Boolean {
                return false
            }

            override fun mapperToDomain(entity: List<RepositoryEntity>): List<TrendingRepo> {
                return entity.mapToDomain()
            }

            override fun databaseFlowable(): Flowable<List<RepositoryEntity>> {
                return dao.fetchRepository(timeToCheck)
            }

            override fun mapperToEntity(dto: List<RepoItemDto>): List<RepositoryEntity> {
                return dto.mapToEntity(timeToCheck)
            }

            override fun netSingle(): Single<List<RepoItemDto>> {
                return api.fetchRepos()
            }
        }.getResult()
    }
}
