package com.alexlepadatu.trendingrepos.data.mappers

import com.alexlepadatu.trendingrepos.data.models.entity.RepositoryAuthor
import com.alexlepadatu.trendingrepos.data.models.entity.RepositoryEntity
import com.alexlepadatu.trendingrepos.domain.models.TrendingRepo
import com.alexlepadatu.trendingrepos.domain.models.TrendingRepoAuthor

internal fun RepositoryEntity.toDomain(): TrendingRepo {
    return TrendingRepo(
        this.name,
        this.description,
        this.language,
        this.avatar,
        this.url,
        this.currentPeriodStars,
        this.forksCount,
        this.starsCount,
        this.builtBy.map { entity -> entity.toDomain() }
    )
}

internal fun RepositoryAuthor.toDomain(): TrendingRepoAuthor {
    return TrendingRepoAuthor(this.username,
        this.href,
        this.avatar
    )
}

internal fun List<RepositoryEntity>.mapToDomain(): List<TrendingRepo> = map {it.toDomain()}