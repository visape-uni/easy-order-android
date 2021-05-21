package edu.uoc.easyorderfront.data

import android.app.Application
import edu.uoc.easyorderfront.data.di.dataModule
import edu.uoc.easyorderfront.ui.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        //Start Koin
        startKoin {
            modules(dataModule)
            modules(uiModule)
            androidContext(this@MyApplication)
        }
    }
}