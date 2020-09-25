package com.alexlepadatu.trendingrepos.domain.useCase

import com.alexlepadatu.trendingrepos.domain.common.ResultState
import com.alexlepadatu.trendingrepos.domain.models.TrendingRepo
import com.alexlepadatu.trendingrepos.domain.repository.TrendingReposRepository
import com.alexlepadatu.trendingrepos.domain.testUtils.InstantAppExecutors
import com.alexlepadatu.trendingrepos.domain.testUtils.ResourceUtil
import com.alexlepadatu.trendingrepos.domain.testUtils.ResourceUtil.Companion.asType
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Flowable
import org.junit.Before

@RunWith(JUnit4::class)
class TrendingReposUseCaseImplTest {
    private lateinit var usecase: TrendingReposUseCaseImpl

    private val mockRepository: TrendingReposRepository = mock()

    @Before
    fun setUp() {
        usecase = TrendingReposUseCaseImpl(InstantAppExecutors(), mockRepository)
    }

    @Test
    fun `test use of repository`() {
        // given
        val result : ResultState<List<TrendingRepo>> = ResultState.Loading(listOf())
        whenever(mockRepository.getTrendingRepositories()).thenReturn(
            Flowable.just(result))

        // when
        usecase.getTrendingRepositories().test()

        // then
        verify(mockRepository).getTrendingRepositories()
    }

    @Test
    fun `repository get loading`() {
        // given
        val result : ResultState<List<TrendingRepo>> = ResultState.Loading(listOf())
        whenever(mockRepository.getTrendingRepositories()).thenReturn(
            Flowable.just(result))

        // when
        val test = usecase.getTrendingRepositories().test()

        // then
        test.assertNoErrors()
        test.assertComplete()
        test.assertValueCount(1)
        test.assertValue(result)
    }

    @Test
    fun `repository get success`() {
        // given
        val repositories : List<TrendingRepo> = ResourceUtil.readFile("repositories.json").asType()
        val result : ResultState<List<TrendingRepo>> = ResultState.Success(repositories)
        whenever(mockRepository.getTrendingRepositories()).thenReturn(
            Flowable.just(result))

        // when
        val test = usecase.getTrendingRepositories().test()

        // then
        verify(mockRepository).getTrendingRepositories()
        test.assertNoErrors()
        test.assertComplete()
        test.assertValueCount(1)
    }

    @Test
    fun `repository get error`() {
        // given
        val result : ResultState<List<TrendingRepo>> = ResultState.Error(Throwable())
        whenever(mockRepository.getTrendingRepositories()).thenReturn(
            Flowable.just(result))

        // when
        val test = usecase.getTrendingRepositories().test()

        // then
        verify(mockRepository).getTrendingRepositories()
        test.assertNoErrors()
        test.assertComplete()
        test.assertValueCount(1)
        test.assertValue(result)
    }

    @Test
    fun `repository get multiple emissions`() {
        // given
        val repositories : List<TrendingRepo> = ResourceUtil.readFile("repositories.json").asType()
        val result : ResultState<List<TrendingRepo>> = ResultState.Success(repositories)
        whenever(mockRepository.getTrendingRepositories()).thenReturn(
            Flowable.just(ResultState.Loading(listOf()), result))

        // when
        val test = usecase.getTrendingRepositories().test()

        // then
        verify(mockRepository).getTrendingRepositories()
        test.assertNoErrors()
        test.assertComplete()
        test.assertValueCount(2)
    }
}