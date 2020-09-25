package com.alexlepadatu.trendingrepos.data.mappers

import com.alexlepadatu.trendingrepos.data.testUtils.DummyData
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class ResponseToEntityTest {

    @Test
    fun `test repository response DTO to entity`() {
        val dto = DummyData.getDummyRepoItemDto()
        val timeFetch = Date().time

        val entity = dto.toEntity(timeFetch)

        Assert.assertTrue(dto.avatar == entity.avatar)
        Assert.assertTrue(dto.description == entity.description)
        Assert.assertTrue(dto.language == entity.language)
        Assert.assertTrue(dto.name == entity.name)
        Assert.assertTrue(dto.url == entity.url)
        Assert.assertTrue(dto.builtBy.size == entity.builtBy.size)
        Assert.assertTrue(dto.starsCount == entity.starsCount)
        Assert.assertTrue(dto.forksCount == entity.forksCount)
        Assert.assertTrue(dto.currentPeriodStars == entity.currentPeriodStars)
    }

    @Test
    fun `test author response DTO to entity`() {
        val dto = DummyData.getDummyRepoAuthorDto()

        val entity = dto.toEntity()

        Assert.assertTrue(dto.avatar == entity.avatar)
        Assert.assertTrue(dto.href == entity.href)
        Assert.assertTrue(dto.username == entity.username)
    }
}