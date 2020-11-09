package dominando.android.livros

import android.app.Application
import dominando.android.data.di.DataModules
import dominando.android.data_fb.di.DataFBModules
import dominando.android.data_room.di.DataRoomModules
import dominando.android.domain.di.DomainModules
import dominando.android.livros.di.AppModule
import dominando.android.presentation.di.PresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BookApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BookApp)
        }

        loadAllModules()
    }

    private fun loadAllModules() {
        AppModule.load()
        DataRoomModules.load()
        DataFBModules.load()
        DataModules.load()
        DomainModules.load()
        PresentationModule.load()
    }
}
