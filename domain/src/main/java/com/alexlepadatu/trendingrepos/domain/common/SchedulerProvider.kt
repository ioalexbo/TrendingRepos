package com.alexlepadatu.trendingrepos.domain.common

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface SchedulerProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
}

class RuntimeSchedulerProvider: SchedulerProvider {
    override fun ui() = AndroidSchedulers.mainThread()
    override fun io() = Schedulers.io()
}