package com.alexlepadatu.trendingrepos.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.alexlepadatu.trendingrepos.data.base.data.BaseDao
import com.alexlepadatu.trendingrepos.data.models.entity.RepositoryEntity
import io.reactivex.Flowable

@Dao
abstract class RepositoryItemDao : BaseDao<RepositoryEntity> {

    @Query("SELECT * FROM repository_items WHERE fetchedAt = :fetchedAt")
    abstract fun fetchRepository(fetchedAt: Long): Flowable<List<RepositoryEntity>>
}