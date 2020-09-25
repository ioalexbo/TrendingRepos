package com.alexlepadatu.trendingrepos.data.mappers

import com.alexlepadatu.trendingrepos.data.testUtils.DummyData
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class EntityToDomainTest {
    @Test
    fun `test repository entity to domain`() {
        val dto = DummyData.getDummyRepoItemDto()
        val timeFetch = Date().time
        val entity = dto.toEntity(timeFetch)
        val domain = entity.toDomain()

        Assert.assertTrue(domain.avatar == entity.avatar)
        Assert.assertTrue(domain.description == entity.description)
        Assert.assertTrue(domain.language == entity.language)
        Assert.assertTrue(domain.name == entity.name)
        Assert.assertTrue(domain.url == entity.url)
        Assert.assertTrue(domain.builtBy.size == entity.builtBy.size)
        Assert.assertTrue(domain.starsCount == entity.starsCount)
        Assert.assertTrue(domain.forksCount == entity.forksCount)
        Assert.assertTrue(domain.currentPeriodStars == entity.currentPeriodStars)
    }

    @Test
    fun `test author response DTO to entity`() {
        val entity = DummyData.getDummyRepoAuthorDto().toEntity()

        val domain = entity.toDomain()

        Assert.assertTrue(domain.avatar == entity.avatar)
        Assert.assertTrue(domain.href == entity.href)
        Assert.assertTrue(domain.username == entity.username)
    }
}