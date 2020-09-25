package com.alexlepadatu.trendingrepos.data.repository

import com.alexlepadatu.trendingrepos.domain.common.ResultState
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.processors.PublishProcessor
import java.util.concurrent.atomic.AtomicReference

abstract class NetworkBoundResource<DTO, Entity, Domain> {
    private var result: Flowable<ResultState<Domain>>

    init {
        val data = AtomicReference<Domain>()
        val processor = PublishProcessor.create<ResultState<Domain>>()

        @Suppress("LeakingThis")
        val flowable : Flowable<Entity> = databaseFlowable().doOnNext {
            data.set(mapperToDomain(it))
        }
            .share()

        result = Flowable.merge(
            flowable.take(1)
                .flatMap {
                    val domainData = mapperToDomain(it)

                    if (shouldFetch(it)) {
                        return@flatMap concatJustFlowable(
                            ResultState.Loading(domainData),
                            handleNetSingle(data.get())
                        )
                    } else {
                        if (alwaysRefresh()) {
                            return@flatMap concatJustFlowable(
                                ResultState.Loading(domainData),
                                handleNetSingle(data.get())
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

    private fun concatJustFlowable(
        d: ResultState<Domain>,
        flowable: Flowable<ResultState<Domain>>
    ) = Flowable.concat(Flowable.just<ResultState<Domain>>(d), flowable)

    private fun handleNetSingle(cachedData: Domain)
            : Flowable<ResultState<Domain>> {
        return netSingle().toFlowable()
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

    protected abstract fun netSingle(): Single<DTO>

    protected abstract fun mapperToEntity(dto: DTO) : Entity

    protected abstract fun databaseFlowable(): Flowable<Entity>

    protected abstract fun mapperToDomain(entity: Entity) : Domain

    // refresh even if cache data is available
    protected abstract fun alwaysRefresh(): Boolean

    protected abstract fun shouldFetch(entity: Entity): Boolean

    protected abstract fun shouldReturnErrorCallback(domain: Domain): Boolean

    protected abstract fun persist(entity: Entity)

    fun getResult(): Flowable<ResultState<Domain>> = result
}