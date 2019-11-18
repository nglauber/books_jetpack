package dominando.android.domain.interactor

import dominando.android.data.BooksRepository
import dominando.android.data.model.Book
import kotlinx.coroutines.flow.Flow

open class ViewBookDetailsUseCase(
    private val repository: BooksRepository
) {
    fun execute(bookId: String): Flow<Book?> {
        return repository.loadBook(bookId)
    }
}
