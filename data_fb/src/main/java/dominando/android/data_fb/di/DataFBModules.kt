package dominando.android.data_fb.di

import dominando.android.data.source.FBData
import dominando.android.data_fb.FBDataImpl
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DataFBModules {

    fun load() {
        loadKoinModules(module {
            factory<FBData> { FBDataImpl() }
        })
    }
}