package com.alexlepadatu.trendingrepos.data.api

import com.alexlepadatu.trendingrepos.data.testUtils.ResourceUtil
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class GitApiTest {
//    @Rule
//    @JvmField
//    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var mockWebServer: MockWebServer

    private lateinit var gitApi: GitApi

    @Before
    fun createService() {

        mockWebServer = MockWebServer()

        gitApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(GitApi::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test path`() {
        enqueueResponse("repositories.json")
        gitApi.fetchRepos().test()
        val request = mockWebServer.takeRequest()

        Assert.assertEquals("/repositories", request.requestUrl?.encodedPath)
    }

    @Test
    fun `test method type`() {
        enqueueResponse("repositories.json")
        gitApi.fetchRepos().test()
        val request = mockWebServer.takeRequest()

        Assert.assertEquals("GET", request.method)
    }

    @Test
    fun `test receive throwable on status code 400`() {
        val mockResponse = MockResponse()
            .setResponseCode(400)
        mockWebServer.enqueue(mockResponse)

        val test = gitApi.fetchRepos().test()
        test.assertError(Throwable::class.java)
    }

    @Test
    fun `test fetched language`() {
        enqueueResponse("repositories.json")

        val result = gitApi.fetchRepos().blockingGet()

        MatcherAssert.assertThat(result[0].language, CoreMatchers.`is`("Kotlin"))
    }

    @Test
    fun `test fetched builtBy`() {
        enqueueResponse("repositories.json")

        val result = gitApi.fetchRepos().blockingGet()

        MatcherAssert.assertThat(result[0].builtBy.size, CoreMatchers.`is`(5))
    }

    @Test
    fun `test no description`() {
        enqueueResponse("repositories_no_description.json")

        val test = gitApi.fetchRepos()
            .test()
        test.awaitTerminalEvent()
        test.assertNoErrors()
        test.assertComplete()
        test.assertValue {
            it[0].description == null
        }
    }

    private fun enqueueResponse(fileName: String) {
        val mockResponse = MockResponse()

        mockWebServer.enqueue(
            mockResponse
                .setBody(ResourceUtil.readFile(fileName))
        )
    }
}