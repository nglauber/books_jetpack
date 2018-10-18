package dominando.android.domain.interactor

import dominando.android.data.BooksRepository
import dominando.android.data.model.Book
import dominando.android.domain.CompletableUseCase
import dominando.android.domain.executor.PostExecutionThread
import io.reactivex.Completable
import java.util.*

open class SaveBookUseCase(private val repository: BooksRepository,
                      postExecutionThread: PostExecutionThread
): CompletableUseCase<Book>(postExecutionThread) {

    override fun buildUseCaseCompletable(params: Book?): Completable {
        return if (params != null && bookIsValid(params)) {
            repository.saveBook(params)
        } else {
            Completable.error(IllegalArgumentException("Book is invalid"))
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