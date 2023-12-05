package com.example.smartsurveillance

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.smartsurveillance.services.SSMessagingService
import com.smartsurveillance_mobile.data.DataStorage
import com.smartsurveillance_mobile.data.FirebaseRepository
import com.example.smartsurveillance.ui.async.gallery.GalleryViewModel
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

val appModules by lazy {
    listOf(baseModule)
}

val baseModule = module {
    single { FirebaseRepository() }
    single { DataStorage(androidApplication().dataStore) }


    viewModel { GalleryViewModel(get(), androidContext().filesDir) }
}

private const val DataStoreName = "SmartSurveillanceDataStore"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DataStoreName)

class App : Application() {


    override fun onCreate() {
        super.onCreate()

        applicationContext.startService(Intent(applicationContext, SSMessagingService::class.java))

        startKoin {
            androidContext(applicationContext)
            androidLogger(
                if (BuildConfig.DEBUG) Level.DEBUG
                else Level.NONE
            )
            modules(appModules)
        }

    }

}
