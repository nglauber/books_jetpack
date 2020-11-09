package dominando.android.domain.di

import dominando.android.domain.interactor.ListBooksUseCase
import dominando.android.domain.interactor.RemoveBookUseCase
import dominando.android.domain.interactor.SaveBookUseCase
import dominando.android.domain.interactor.ViewBookDetailsUseCase
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DomainModules {

    fun load() {
        loadKoinModules(module {
            factory { ListBooksUseCase(repository = get()) }
            factory { RemoveBookUseCase(repository = get()) }
            factory { ViewBookDetailsUseCase(repository = get()) }
            factory { SaveBookUseCase(repository = get()) }
        })
    }
}