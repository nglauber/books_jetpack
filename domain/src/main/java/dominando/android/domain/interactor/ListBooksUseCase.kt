package dominando.android.domain.interactor

import dominando.android.data.BooksRepository
import dominando.android.data.model.Book
import kotlinx.coroutines.flow.Flow

open class ListBooksUseCase(
    private val repository: BooksRepository
) {
    fun execute(): Flow<List<Book>> {
        return repository.loadBooks()
    }
}
