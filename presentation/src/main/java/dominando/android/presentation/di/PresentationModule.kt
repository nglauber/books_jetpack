package dominando.android.presentation.di

import dominando.android.presentation.BookDetailsViewModel
import dominando.android.presentation.BookFormViewModel
import dominando.android.presentation.BookListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object PresentationModule {

    fun load() {
        loadKoinModules(module {
            viewModel {
                BookListViewModel(removeBookUseCase = get(), loadBooksUseCase = get())
            }

            viewModel {
                (bookId: String) -> BookDetailsViewModel(bookId, viewBookDetailsUseCase = get())
            }

            viewModel {
                BookFormViewModel(saveBookUseCase = get())
            }
        })
    }
}