package com.cbl.base

import android.app.Application
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

open class BaseApp : Application() {
    companion object{
        lateinit var CONTEXT: Context
        val scope= CoroutineScope(SupervisorJob() + Dispatchers.Main)

    }
    override fun onCreate() {
        super.onCreate()
        CONTEXT=this
        Timber.plant(Timber.DebugTree())
        Timber.i("onCreate")
        scope.launch(Dispatchers.IO) {
            MediaUtil.getData()
        }
    }
}