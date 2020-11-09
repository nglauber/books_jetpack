package dominando.android.domain.interactor

import dominando.android.domain.entity.Book
import dominando.android.domain.repository.BooksRepository
import kotlinx.coroutines.flow.Flow

open class ViewBookDetailsUseCase(private val repository: BooksRepository) {

    fun execute(bookId: String): Flow<Book?> {
        return repository.loadBook(bookId)
    }
}
