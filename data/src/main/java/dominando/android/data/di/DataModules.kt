package dominando.android.data.di

import dominando.android.data.mapper.BooksDataMapper
import dominando.android.data.mapper.BooksMapper
import dominando.android.data.repository.BooksRepositoryImpl
import dominando.android.domain.repository.BooksRepository
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DataModules {

    fun load() {
        loadKoinModules(module {
            factory<BooksRepository> {
                BooksRepositoryImpl(
                    localData = get(),
                    entityMapper = BooksMapper(),
                    dataMapper = BooksDataMapper()
                )
            }
        })
    }
}