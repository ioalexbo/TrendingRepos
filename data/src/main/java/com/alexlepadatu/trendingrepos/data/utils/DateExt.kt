package com.alexlepadatu.trendingrepos.data.utils

import java.util.*

class DateExt {
    companion object {

        fun timelessDate(): Date {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            return calendar.time
        }
    }
}