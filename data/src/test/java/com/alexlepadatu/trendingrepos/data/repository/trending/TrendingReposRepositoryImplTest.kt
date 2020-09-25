package com.alexlepadatu.trendingrepos.data.repository.trending

import com.alexlepadatu.inggithub.data.RepoItemDto
import com.alexlepadatu.trendingrepos.data.api.GitApi
import com.alexlepadatu.trendingrepos.data.database.dao.RepositoryItemDao
import com.alexlepadatu.trendingrepos.data.mappers.mapToDomain
import com.alexlepadatu.trendingrepos.data.mappers.mapToEntity
import com.alexlepadatu.trendingrepos.data.models.entity.RepositoryEntity
import com.alexlepadatu.trendingrepos.data.testUtils.DummyData
import com.alexlepadatu.trendingrepos.data.utils.DateExt
import com.alexlepadatu.trendingrepos.domain.common.ResultState
import com.alexlepadatu.trendingrepos.domain.models.TrendingRepo
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyZeroInteractions

@RunWith(JUnit4::class)
class TrendingReposRepositoryImplTest {
    private lateinit var repository: TrendingReposRepositoryImpl

    private val api: GitApi = mock()
    private val dao: RepositoryItemDao = mock()

    private val time = DateExt.timelessDate().time
    private val dummyRepoDtos: List<RepoItemDto> = DummyData.getDummyRepoItemDtos()
    private val dummyRepoEntities : List<RepositoryEntity> = dummyRepoDtos.mapToEntity(time)
    private val dummyTrendingRepos: List<TrendingRepo> = dummyRepoEntities.mapToDomain()
    private val throwable = Throwable()

    @Before
    fun setUp() {
        repository = TrendingReposRepositoryImpl(api, dao)
    }

    @Test
    fun `test don't use network api when cached data is available`() {
        // given
        whenever(dao.fetchRepository(time)).thenReturn(Flowable.just(dummyRepoEntities))
        whenever(api.fetchRepos()).thenReturn(Single.just(listOf()))

        // when
        repository.getTrendingRepositories().test()

        // test
        verifyZeroInteractions(api)
    }

    @Test
    fun `test repository values when using data from cache`() {
        // given
        whenever(dao.fetchRepository(time)).thenReturn(Flowable.just(dummyRepoEntities))

        // when
        val test = repository.getTrendingRepositories().test()

        // test
        val expectedLoading: ResultState<List<TrendingRepo>> = ResultState.Loading(dummyTrendingRepos)
        val expectedSuccess: ResultState<List<TrendingRepo>> = ResultState.Success(dummyTrendingRepos)
        test.assertValues(expectedLoading, expectedSuccess)
    }

    @Test
    fun `test use network api when no cache values`() {
        // given
        whenever(dao.fetchRepository(time)).thenReturn(Flowable.just(listOf()))
        whenever(api.fetchRepos()).thenReturn(Single.just(dummyRepoDtos))

        // when
        repository.getTrendingRepositories().test()

        // test
        verify(api).fetchRepos()
    }

    @Test
    fun `test repository values when no cache values`() {
        whenever(dao.fetchRepository(time)).thenReturn(Flowable.just(listOf()))
        whenever(api.fetchRepos()).thenReturn(Single.just(dummyRepoDtos))
        whenever(dao.fetchRepository(time)).thenReturn(Flowable.just(dummyRepoEntities))

        // when
        val test = repository.getTrendingRepositories().test()

        // test
        val expectedLoading: ResultState<List<TrendingRepo>> = ResultState.Loading(dummyTrendingRepos)
        val expectedSuccess: ResultState<List<TrendingRepo>> = ResultState.Success(dummyTrendingRepos)
        test.assertValues(expectedLoading, expectedSuccess)
    }

    @Test
    fun `test cached data throws error then return error`() {
        // given
        whenever(dao.fetchRepository(time)).thenReturn(Flowable.error(throwable))
        whenever(api.fetchRepos()).thenReturn(Single.just(dummyRepoDtos))

        // when
        val test = repository.getTrendingRepositories().test()

        // test
        val expected: ResultState<List<TrendingRepo>> = ResultState.Error(throwable)
        test.assertValue(expected)
    }

    @Test
    fun `test network api throws error then return error`() {
        // given
        whenever(dao.fetchRepository(time)).thenReturn(Flowable.just(listOf()))
        whenever(api.fetchRepos()).thenReturn(Single.error(throwable))

        // when
        val test = repository.getTrendingRepositories().test()

        // test
        val expectedLoading: ResultState<List<TrendingRepo>> = ResultState.Loading(listOf())
        val expectedError: ResultState<List<TrendingRepo>> = ResultState.Error(throwable, listOf())
        test.assertValues(expectedLoading, expectedError)
    }
}