package com.alexlepadatu.trendingrepos.data.testUtils

import com.alexlepadatu.ing.gitrepos.data.model.RepoAuthorDto
import com.alexlepadatu.inggithub.data.RepoItemDto
import com.alexlepadatu.trendingrepos.data.mappers.toDomain
import com.alexlepadatu.trendingrepos.data.mappers.toEntity
import com.alexlepadatu.trendingrepos.data.testUtils.ResourceUtil.Companion.asType
import com.alexlepadatu.trendingrepos.domain.models.TrendingRepo

class DummyData {
    companion object {
        fun getDummyRepoItemDto(fileName: String = "repository.json"): RepoItemDto {
            return ResourceUtil.readFile(fileName).asType()
        }

        fun getDummyRepoItemDtos(): List<RepoItemDto> {
            return listOf(getDummyRepoItemDto())
        }

        fun getDummyRepoAuthorDto(fileName: String = "author.json"): RepoAuthorDto {
            val input = ResourceUtil.readFile(fileName)
            return input.asType()
        }
    }
}