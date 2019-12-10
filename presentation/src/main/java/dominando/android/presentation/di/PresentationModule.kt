package dominando.android.presentation.di

import dominando.android.data.BooksRepository
import dominando.android.data_room.LocalFileHelper
import dominando.android.data_room.RoomRepository
import dominando.android.data_room.database.AppDatabase
import dominando.android.domain.interactor.ListBooksUseCase
import dominando.android.domain.interactor.RemoveBookUseCase
import dominando.android.domain.interactor.SaveBookUseCase
import dominando.android.domain.interactor.ViewBookDetailsUseCase
import dominando.android.presentation.BookDetailsViewModel
import dominando.android.presentation.BookFormViewModel
import dominando.android.presentation.BookListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    single {
        RoomRepository(AppDatabase.getDatabase(context = get()), LocalFileHelper()) as BooksRepository
        // FbRepository() as BooksRepository
    }

    factory {
        ListBooksUseCase(repository = get())
    }

    factory {
        RemoveBookUseCase(repository = get())
    }

    factory {
        ViewBookDetailsUseCase(repository = get())
    }

    factory {
        SaveBookUseCase(repository = get())
    }

    viewModel {
        BookListViewModel(removeBookUseCase = get(), loadBooksUseCase = get())
    }

    viewModel {
        (bookId: String) -> BookDetailsViewModel(bookId, viewBookDetailsUseCase = get())
    }

    viewModel {
        BookFormViewModel(saveBookUseCase = get())
    }
}
