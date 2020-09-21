package com.alexlepadatu.trendingrepos.data.database

import androidx.room.TypeConverter
import com.alexlepadatu.trendingrepos.data.models.entity.RepositoryAuthor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ComplexTypeConverters {

    @TypeConverter
    fun fromStringToListRepositoryAuthor(value: String): List<RepositoryAuthor> {
        val type = object: TypeToken<List<RepositoryAuthor>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromListRepositoryAuthor(list: List<RepositoryAuthor>): String {
        val type = object: TypeToken<List<RepositoryAuthor>>() {}.type
        return Gson().toJson(list, type)
    }
}