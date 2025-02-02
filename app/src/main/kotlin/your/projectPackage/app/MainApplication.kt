package your.projectPackage.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import your.projectPackage.common.initLogger

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initLogger()
    }
}
