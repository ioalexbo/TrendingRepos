package com.alexlepadatu.trendingrepos.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alexlepadatu.trendingrepos.data.database.dao.RepositoryItemDao
import com.alexlepadatu.trendingrepos.data.models.entity.RepositoryEntity

@Database(entities = [RepositoryEntity::class],
    version = 1,
    exportSchema = false)
@TypeConverters(ComplexTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun repositoryItemDao(): RepositoryItemDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        private const val DATABASE_NAME = "trending-repos-db"

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}