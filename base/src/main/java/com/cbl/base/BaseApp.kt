package com.cbl.base

import android.app.Application
import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.provider.MediaStore
import androidx.lifecycle.*
import com.cbl.base.bean.GreenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import timber.log.Timber

open class BaseApp : Application() {
    val refresh = MutableStateFlow(SystemClock.uptimeMillis())
    val mainHandler = Handler(Looper.getMainLooper())

    companion object {
        lateinit var CONTEXT: Context

        //        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        val scope = ProcessLifecycleOwner.get().lifecycleScope

    }

    fun requestData() {
        scope.launch {
            val time = SystemClock.uptimeMillis();
            Timber.i("onChange time-->$time Thread--> ${Thread.currentThread().name}")
            refresh.emit(time)
            Timber.i("onChange emit over time-->$time")
        }
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = this
        Timber.plant(Timber.DebugTree())
        Timber.i("onCreate")
        GreenManager.initGreenManager(this)


        val contentObserver = object : ContentObserver(mainHandler) {
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                Timber.i("requestData by contentObserver")
                requestData()
            }

        }
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            ProcessLifecycleOwner.get().repeatOnLifecycle(Lifecycle.State.RESUMED) {
                Timber.i("requestData by ProcessLifecycleOwner")
                requestData()
            }
        }
        contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            true,
            contentObserver
        )
        contentResolver.registerContentObserver(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            true,
            contentObserver
        )
        scope.launch(Dispatchers.IO) {
            refresh.collectLatest {
                Timber.i("collectLatest it-->$it  Thread--> ${Thread.currentThread().name}")
                MediaUtil.getData()
            }
        }
    }
}