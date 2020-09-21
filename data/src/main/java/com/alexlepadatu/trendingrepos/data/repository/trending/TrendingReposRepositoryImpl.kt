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
        val timeToCheck : Long = DateExt.timelessDate().time

        val apiCall = api.fetchRepos()
        val mapperToEntity : (List<RepoItemDto>) -> List<RepositoryEntity> = { it.mapToEntity(timeToCheck) }

        val dbFlowable = dao.fetchRepository(timeToCheck)
        val mapperToDomain : (List<RepositoryEntity>) -> List<TrendingRepo> = { it.mapToDomain() }

        val persist : (items: List<RepositoryEntity>) -> Unit = {
            Log.e("TrendingReposRepository", "persist data: ${it.size}")
            dao.insertAll(*it.toTypedArray())
        }

        val shouldFetch : (List<RepositoryEntity>) -> Boolean = {
            it.isNullOrEmpty()
        }

        val shouldReturnErrorCallback : (List<TrendingRepo>) -> Boolean = {
            it.isNullOrEmpty()
        }

        return performGet(apiCall,
            mapperToEntity,
            dbFlowable,
            mapperToDomain,
            false,
            shouldFetch,
            shouldReturnErrorCallback,
            persist)
    }



//    private fun <DTO, Entity, Domain> performGet(
//        netSingle: Single<List<DTO>>,
//        mapperToEntity : (List<DTO>) -> List<Entity>,
//        databaseFlowable: Flowable<List<Entity>>,
//        mapperToDomain : (List<Entity>) -> List<Domain>,
//        alwaysRefresh: Boolean,
//        persist: (list: List<Entity>) -> Unit): Flowable<ResultState<List<Domain>>> {
//
//        val data = AtomicReference<List<Domain>>()
//        val processor = PublishProcessor.create<ResultState<List<Domain>>>()
//        val flowable : Flowable<List<Entity>> = databaseFlowable.doOnNext {
//            data.set(mapperToDomain(it))
//        }
//        .share()
//
//        return Flowable.merge(
//            flowable.take(1)
//                    .flatMap {
//                        if (it.isEmpty()) {
//                            return@flatMap concatJustFlowable(
//                                ResultState.Loading(it) as ResultState<List<Domain>>,
//                                handleNetSingle(netSingle, mapperToEntity, persist, data.get())
//                            )
//                        } else {
//                            if (alwaysRefresh) {
//                                return@flatMap concatJustFlowable(
//                                    ResultState.Loading(it) as ResultState<List<Domain>>,
//                                    handleNetSingle(netSingle, mapperToEntity, persist, data.get())
//                                )
//                            }
//
//                            return@flatMap Flowable.just<ResultState<List<Domain>>>(
//                                ResultState.Loading(it) as ResultState<List<Domain>>,
//                                ResultState.Success(it) as ResultState<List<Domain>>)
//                        }
//                    },
//            flowable.skip(1)
//                    .flatMap {
//                        if (it.isNotEmpty()) {
//                            return@flatMap concatJustFlowable(
//                                ResultState.Success(it) as ResultState<List<Domain>>,
//                                processor)
//                        } else {
//                            return@flatMap processor
//                        }
//                    })
//                .onErrorResumeNext(io.reactivex.functions.Function {
//                    concatJustFlowable(ResultState.Error(it, data.get()), processor)
//                })
//    }
//
//    private fun <Domain> concatJustFlowable(
//        d: ResultState<List<Domain>>,
//        flowable: Flowable<ResultState<List<Domain>>>
//    ) = Flowable.concat(Flowable.just<ResultState<List<Domain>>>(d), flowable)
//
//    private fun <DTO, Entity, Domain> handleNetSingle(
//        netSingle: Single<List<DTO>>,
//        mapperToEntity : (List<DTO>) -> List<Entity>,
//        persist: (List<Entity>) -> Unit,
//        cachedData: List<Domain>
//    ): Flowable<ResultState<List<Domain>>> {
//        return netSingle.toFlowable()
//            .flatMap {
//                val entitiesToPersist = mapperToEntity(it)
//                persist(entitiesToPersist)
//                return@flatMap Completable.complete().toFlowable<ResultState<List<Domain>>>()
//        }.onErrorReturn {
//                if (cachedData.isNullOrEmpty())
//                    ResultState.Error(it, cachedData)
//                else
//                    ResultState.Success(cachedData)
//        }
//    }
}







//class TrendingReposRepositoryImpl (private val api: GitApi,
//                                   private val dao: RepositoryItemDao
//): TrendingReposRepository {
//
//    override fun getTrendingRepositories(): Flowable<ResultState<List<TrendingRepo>>> {
//        return addLoading(fetchFlow())
//    }
//
//    private fun fetchFlow():Flowable<ResultState<List<TrendingRepo>>> {
//        val timeToCheck : Long = DateExt.timelessDate().time
//
//        return dao.fetchRepository(timeToCheck)
//            .flatMap { items ->
//                val listTrendingRepo : List<TrendingRepo> = mutableListOf<TrendingRepo>().also {
//                    it.addAll(items.map { it.toDomain() })
//                }.toList()
//
//                val publisher = ReplaySubject.create<ResultState<List<TrendingRepo>>>()
//                publisher.onNext(ResultState.Success(listTrendingRepo))
//
////                if (items.isNullOrEmpty()) {
////                    api.fetchRepos()
////                        .map { dtos ->
////                            return@map mutableListOf<RepositoryEntity>().also {
////                                val time = timeToCheck
////                                it.addAll(dtos.map { it.toEntity(time) })
////                            }.toList()
////                        }
////                        .subscribe( {
////                            dao.insertAll(*it.toTypedArray())
////                            Log.e("TrendingReposRepository", "fetched from api: ${it.size}")
////                        }, {
//////                            flowable = flowable.concatWith(Flowable.just<ResultState<List<TrendingRepo>>>(ResultState.Error(it)))
////                            publisher.onNext(ResultState.Error(it, listOf()))
////                            Log.e("TrendingReposRepository", "error fetched from api: $it")
////                        })
////                }
//
//                return@flatMap publisher.toFlowable(BackpressureStrategy.BUFFER)
//            }
//            .flatMap { resultState ->
//                if (resultState is ResultState.Success && resultState.data.isNullOrEmpty()) {
//                    return@flatMap api.fetchRepos()
//                        .map { dtos ->
//                            val list: List<RepositoryEntity> = mutableListOf<RepositoryEntity>().also {
//                                val time = timeToCheck
//                                it.addAll(dtos.map { it.toEntity(time) })
//                            }.toList()
//
//                            return@map ResultState.Success(list) as ResultState<List<RepositoryEntity>>
//                        }
//                        .toFlowable()
//                        .doOnNext {
//                            if (it is ResultState.Success)
//                                dao.insertAll(*it.data.toTypedArray())
//                        }
//                        .onErrorReturn { ResultState.Error(it, listOf()) }
//                        .filter { it is ResultState.Error }
//                        .map {
//                            it as ResultState.Error
//                            ResultState.Error(it.throwable, it.data?.map { it.toDomain() } )
//                        }
//                }
//
//                return@flatMap Flowable.just<ResultState<List<TrendingRepo>>>(resultState)
//            }
//
////        return dao.fetchRepository(timeToCheck)
////            .flatMap { items ->
////                val listTrendingRepo : List<TrendingRepo> = mutableListOf<TrendingRepo>().also {
////                    it.addAll(items.map { it.toDomain() })
////                }.toList()
////
////                val publisher = ReplaySubject.create<ResultState<List<TrendingRepo>>>()
////                publisher.onNext(ResultState.Success(listTrendingRepo))
////
//////                var flowable : Flowable<ResultState<List<TrendingRepo>>>
//////                        = Flowable.just<ResultState<List<TrendingRepo>>>(ResultState.Success(listTrendingRepo))
////
////                if (items.isNullOrEmpty()) {
////                    api.fetchRepos()
////                        .map { dtos ->
////                            return@map mutableListOf<RepositoryEntity>().also {
////                                val time = timeToCheck
////                                it.addAll(dtos.map { it.toEntity(time) })
////                            }.toList()
////                        }
////                        .subscribe( {
////                            dao.insertAll(*it.toTypedArray())
////                            Log.e("TrendingReposRepository", "fetched from api: ${it.size}")
////                        }, {
//////                            flowable = flowable.concatWith(Flowable.just<ResultState<List<TrendingRepo>>>(ResultState.Error(it)))
////                            publisher.onNext(ResultState.Error(it))
////                            Log.e("TrendingReposRepository", "error fetched from api: $it")
////                        })
////                }
////
////                return@flatMap publisher.toFlowable(BackpressureStrategy.BUFFER)
//////                return@flatMap flowable
////            }
//    }
//
//    private fun addLoading(flow: Flowable<ResultState<List<TrendingRepo>>>) : Flowable<ResultState<List<TrendingRepo>>> {
//        return flow
//            .startWith(Flowable.just(ResultState.Loading(listOf())))
//    }
//}
