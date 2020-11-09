package dominando.android.domain.interactor

import dominando.android.domain.entity.Book
import dominando.android.domain.repository.BooksRepository
import java.util.Calendar

open class SaveBookUseCase(private val repository: BooksRepository) {

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
