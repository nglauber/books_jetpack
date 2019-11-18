package dominando.android.livros

import android.app.Application
import dominando.android.livros.di.appModule
import dominando.android.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BookApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BookApp)
            modules(
                    listOf(
                            appModule, presentationModule
                    )
            )
        }
    }
}
