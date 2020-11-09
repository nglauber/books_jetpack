package dominando.android.domain.interactor

import dominando.android.domain.entity.Book
import dominando.android.domain.repository.BooksRepository
import kotlinx.coroutines.flow.Flow

open class ListBooksUseCase(private val repository: BooksRepository) {

    fun execute(): Flow<List<Book>> {
        return repository.loadBooks()
    }
}
