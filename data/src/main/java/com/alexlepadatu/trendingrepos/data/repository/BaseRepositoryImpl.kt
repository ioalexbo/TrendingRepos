package com.alexlepadatu.trendingrepos.data.repository

import io.reactivex.*
import io.reactivex.processors.PublishProcessor
import com.alexlepadatu.trendingrepos.domain.common.ResultState
import java.util.concurrent.atomic.AtomicReference

/**
 * @param DTO the type returned by network call
 * @param Entity the entity that is related to this repository
 * @param Domain the type returned by the repo
 */
abstract class BaseRepositoryImpl<DTO, Entity, Domain> {

    /**
     * @param D a data type
     * @param databaseFlowable a flowable to load an entity from database.
     * @param netSingle a single to load an entity from server.
     * @param persist a high order function to persist an entity.
     * @param alwaysRefresh if you want to always fetch from network call, or use the cached values
     */
    protected fun performGet(
        netSingle: Single<DTO>,
        mapperToEntity : (DTO) -> Entity,
        databaseFlowable: Flowable<Entity>,
        mapperToDomain : (Entity) -> Domain,
        alwaysRefresh: Boolean,
        shouldFetch: (Entity) -> Boolean,
        shouldReturnErrorCallback: (Domain) -> Boolean,
        persist: (list: Entity) -> Unit): Flowable<ResultState<Domain>> {

        val data = AtomicReference<Domain>()
        val processor = PublishProcessor.create<ResultState<Domain>>()
        val flowable : Flowable<Entity> = databaseFlowable.doOnNext {
            data.set(mapperToDomain(it))
        }
            .share()

        return Flowable.merge(
            flowable.take(1)
                .flatMap {
                    val domainData = mapperToDomain(it)

                    if (shouldFetch(it)) {
                        return@flatMap concatJustFlowable(
                            ResultState.Loading(domainData),
                            handleNetSingle(netSingle, mapperToEntity, persist, data.get(), shouldReturnErrorCallback)
                        )
                    } else {
                        if (alwaysRefresh) {
                            return@flatMap concatJustFlowable(
                                ResultState.Loading(domainData),
                                handleNetSingle(netSingle, mapperToEntity, persist, data.get(), shouldReturnErrorCallback)
                            )
                        }

                        return@flatMap Flowable.just<ResultState<Domain>>(
                            ResultState.Loading(domainData),
                            ResultState.Success(domainData))
                    }
                },
            flowable.skip(1)
                .flatMap {
                    if (shouldFetch(it)) {
                        return@flatMap processor
                    } else {
                        return@flatMap concatJustFlowable(
                            ResultState.Success(mapperToDomain(it)),
                            processor)
                    }
                })
            .onErrorResumeNext(io.reactivex.functions.Function {
                concatJustFlowable(ResultState.Error(it, data.get()), processor)
            })
    }

    private fun <Domain> concatJustFlowable(
        d: ResultState<Domain>,
        flowable: Flowable<ResultState<Domain>>
    ) = Flowable.concat(Flowable.just<ResultState<Domain>>(d), flowable)

    private fun <DTO, Entity, Domain> handleNetSingle(
        netSingle: Single<DTO>,
        mapperToEntity : (DTO) -> Entity,
        persist: (Entity) -> Unit,
        cachedData: Domain,
        shouldReturnErrorCallback: (Domain) -> Boolean
    ): Flowable<ResultState<Domain>> {
        return netSingle.toFlowable()
            .flatMap {
                val entitiesToPersist = mapperToEntity(it)
                persist(entitiesToPersist)
                return@flatMap Completable.complete().toFlowable<ResultState<Domain>>()
            }.onErrorReturn {
                if (shouldReturnErrorCallback(cachedData))
                    ResultState.Error(it, cachedData)
                else
                    ResultState.Success(cachedData)
            }
    }

//    protected fun <D> perform(
//            databaseFlowable: Flowable<List<D>>,
//            netSingle: Single<D>,
//            persist: (D) -> Unit
//    ): Flowable<ResultState<D>> {
//
//        val data = AtomicReference<D>()
//        val processor = PublishProcessor.create<ResultState<D>>()
//        val f = databaseFlowable.doOnNext { data.set(it.firstOrNull()) }.share()
//
//        return Flowable.merge(f.take(1)
//                .flatMap {
//                    if (it.isEmpty()) {
//                        return@flatMap handleNetSingle(netSingle, persist, data.get())
//                    } else {
//                        return@flatMap concatJustFlowable(
//                                ResultState.Loading(it.first()),
//                                handleNetSingle(netSingle, persist, data.get())
//                        )
//                    }
//                }, f.skip(1)
//                .flatMap {
//                    if (it.isNotEmpty()) {
//                        concatJustFlowable(ResultState.Success(it.first()), processor)
//                    } else {
//                        processor
//                    }
//                })
//                .onErrorResumeNext(io.reactivex.functions.Function {
//                    concatJustFlowable(ResultState.Error(it, data.get()), processor)
//                })
//    }
//
//    private fun <D> concatJustFlowable(
//            d: ResultState<D>,
//            flowable: Flowable<ResultState<D>>
//    ) = Flowable.concat(Flowable.just<ResultState<D>>(d), flowable)
//
//    private fun <D> handleNetSingle(
//            netSingle: Single<D>,
//            persist: (D) -> Unit,
//            cachedData: D
//    ): Flowable<ResultState<D>> = netSingle.toFlowable().flatMap {
//        persist(it)
//        Completable.complete().toFlowable<ResultState<D>>()
//    }.onErrorReturn {
//        ResultState.Error(it, cachedData)
//    }
}