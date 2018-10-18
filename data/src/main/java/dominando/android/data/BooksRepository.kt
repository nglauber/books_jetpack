package dominando.android.data

import dominando.android.data.model.Book
import io.reactivex.Completable
import io.reactivex.Flowable

interface BooksRepository {
    fun saveBook(book: Book): Completable
    fun loadBooks(): Flowable<List<Book>>
    fun loadBook(bookId: String): Flowable<Book>
    fun remove(book: Book): Completable
}