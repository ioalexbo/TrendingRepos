package com.alexlepadatu.trendingrepos.data.mappers

import com.alexlepadatu.ing.gitrepos.data.model.RepoAuthorDto
import com.alexlepadatu.inggithub.data.RepoItemDto
import com.alexlepadatu.trendingrepos.data.models.entity.RepositoryAuthor
import com.alexlepadatu.trendingrepos.data.models.entity.RepositoryEntity
import com.alexlepadatu.trendingrepos.domain.models.TrendingRepo

internal fun RepoItemDto.toEntity(timeFetch: Long): RepositoryEntity {
    return RepositoryEntity(
        name = this.name,
        description = this.description,
        language = this.language,
        avatar = this.avatar,
        url = this.url,
        currentPeriodStars = this.currentPeriodStars,
        forksCount = this.forksCount,
        starsCount = this.starsCount,
        builtBy = this.builtBy.map { dto -> dto.toEntity() },
        fetchedAt = timeFetch
    )
}

internal fun RepoAuthorDto.toEntity(): RepositoryAuthor {
    return RepositoryAuthor(this.username,
        this.href,
        this.avatar
    )
}

internal fun List<RepoItemDto>.mapToEntity(timeFetch: Long): List<RepositoryEntity> = map {it.toEntity(timeFetch)}