package dominando.android.data_room

import dominando.android.data.BooksRepository
import dominando.android.data.model.Book
import dominando.android.data.util.FileHelper
import dominando.android.data_room.database.AppDatabase
import io.reactivex.Completable
import io.reactivex.Flowable
import java.lang.RuntimeException
import java.util.*
import dominando.android.data_room.entity.Book as BookEntity

class RoomRepository(db: AppDatabase,
                     private val fileHelper: FileHelper) : BooksRepository {

    private val bookDao = db.bookDao()

    override fun saveBook(book: Book): Completable {
        if (book.id.isBlank()) {
            book.id = UUID.randomUUID().toString()
        }
        return if (fileHelper.saveCover(book)) {
            bookDao.save(BookConverter.fromData(book))
        } else {
            Completable.error(RuntimeException("Error to save book's cover."))
        }
    }

    override fun loadBooks(): Flowable<List<Book>> {
        return bookDao.bookByTitle()
                .map { books ->
                    books.map { book ->
                        BookConverter.toData(book)
                    }
                }
    }

    override fun loadBook(bookId: String): Flowable<Book> {
        return bookDao.bookById(bookId)
                .map { book -> BookConverter.toData(book) }
    }

    override fun remove(book: Book): Completable {
        return bookDao.delete(BookConverter.fromData(book))
                .doOnComplete {
                    try {
                        if (fileHelper.deleteExistingCover(book)) {
                            Completable.complete()
                        } else {
                            Completable.error(Exception("Fail to remove book's cover"))
                        }
                    } catch (e: Exception) {
                        Completable.error(e)
                    }
                }
    }
}