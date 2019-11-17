package dominando.android.domain.interactor

import dominando.android.data.BooksRepository
import dominando.android.data.model.Book
import java.util.*

open class SaveBookUseCase(
        private val repository: BooksRepository
) {
    suspend fun execute(params: Book) {
        return if (bookIsValid(params)) {
            repository.saveBook(params)
        } else {
            throw IllegalArgumentException("Book is invalid")
        }
    }

    private fun bookIsValid(book: Book): Boolean {
        return (
            book.title.isNotBlank() &&
            book.author.isNotBlank() &&
            book.pages > 0 &&
            book.year > 1900 && book.year <= Calendar.getInstance().get(Calendar.YEAR)
        )
    }
}