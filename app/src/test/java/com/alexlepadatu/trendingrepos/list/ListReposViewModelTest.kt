package com.alexlepadatu.trendingrepos.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alexlepadatu.trendingrepos.domain.common.ResultState
import com.alexlepadatu.trendingrepos.domain.models.TrendingRepo
import com.alexlepadatu.trendingrepos.domain.useCase.TrendingReposUseCase
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Flowable
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@RunWith(JUnit4::class)
class ListReposViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val mockUseCase: TrendingReposUseCase = mock()

    private lateinit var viewModel: ListReposViewModel

    @Test
    fun `test use of useCase`() {
        // given
        val result : ResultState<List<TrendingRepo>> = ResultState.Success(listOf())
        whenever(mockUseCase.getTrendingRepositories()).thenReturn(Flowable.just(result))

        // when
        viewModel = ListReposViewModel(mockUseCase)

        // test
        verify(mockUseCase).getTrendingRepositories()
    }

    @Test
    fun `test receive emission`() {
        // given
        val result : ResultState<List<TrendingRepo>> = ResultState.Loading(listOf())
        whenever(mockUseCase.getTrendingRepositories()).thenReturn(Flowable.just(result))

        // when
        viewModel = ListReposViewModel(mockUseCase)
        val observer = mock<Observer<ResultState<List<TrendingRepo>>>>()
        viewModel.trendingRepos.observeForever(observer)

        // test
        Mockito.verify(observer, times(1)).onChanged(any())
    }

    @Test
    fun `test receive value`() {
        // given
        val result : ResultState<List<TrendingRepo>> = ResultState.Loading(listOf())
        whenever(mockUseCase.getTrendingRepositories()).thenReturn(Flowable.just(result))

        // when
        viewModel = ListReposViewModel(mockUseCase)
        val observer = mock<Observer<ResultState<List<TrendingRepo>>>>()
        viewModel.trendingRepos.observeForever(observer)

        // test
        Mockito.verify(observer).onChanged(result)
    }
}