package dominando.android.data_room

import dominando.android.data.BooksRepository
import dominando.android.data.model.Book
import dominando.android.data.util.FileHelper
import dominando.android.data_room.database.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.RuntimeException
import java.util.*

class RoomRepository(db: AppDatabase,
                     private val fileHelper: FileHelper) : BooksRepository {

    private val bookDao = db.bookDao()

    override suspend fun saveBook(book: Book) {
        if (book.id.isBlank()) {
            book.id = UUID.randomUUID().toString()
        }
        return if (fileHelper.saveCover(book)) {
            bookDao.save(BookConverter.fromData(book))
        } else {
            throw RuntimeException("Error to save book's cover.")
        }
    }

    override fun loadBooks(): Flow<List<Book>> {
        return bookDao.bookByTitle()
                .map { books ->
                    books.map { book ->
                        BookConverter.toData(book)
                    }
                }
    }

    override fun loadBook(bookId: String): Flow<Book?> {
        return bookDao.bookById(bookId)
                .map { book -> BookConverter.toData(book) }
    }

    override suspend fun remove(book: Book) {
        bookDao.delete(BookConverter.fromData(book))
        fileHelper.deleteExistingCover(book)
    }
}