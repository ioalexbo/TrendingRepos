package com.alexlepadatu.trendingrepos.domain.testUtils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okio.Okio
import okio.buffer
import okio.source

class ResourceUtil {
    companion object {

        fun readFile(fileName: String) : String {
            val inputStream = ResourceUtil::class.java.classLoader?.getResourceAsStream(fileName)
            val source = inputStream!!.source().buffer()
            return source.readString(Charsets.UTF_8)
        }

        inline fun <reified T> String.asType(): T {
            return Gson().fromJson(this, object: TypeToken<T>(){}.type)
        }
    }
}