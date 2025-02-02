package me.tbsten.tripleTriad.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import me.tbsten.tripleTriad.common.initLogger

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initLogger()
    }
}
