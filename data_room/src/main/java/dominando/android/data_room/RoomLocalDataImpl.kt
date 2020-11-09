package dominando.android.data_room

import dominando.android.data.model.BookData
import dominando.android.data.source.RoomLocalData
import dominando.android.data_room.filehelper.FileHelper
import dominando.android.data_room.database.AppDatabase
import java.lang.RuntimeException
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class RoomLocalDataImpl(
    db: AppDatabase,
    private val fileHelper: FileHelper
) : RoomLocalData {

    private val bookDao = db.bookDao()

    override suspend fun saveBook(book: BookData) {
        if (book.id.isBlank()) {
            book.id = UUID.randomUUID().toString()
        }
        return if (fileHelper.saveCover(book)) {
            bookDao.save(BookConverter.fromData(book))
        } else {
            throw RuntimeException("Error to save book's cover.")
        }
    }

    override fun loadBooks(): Flow<List<BookData>> {
        return bookDao.bookByTitle()
                .map { books ->
                    books.map { book ->
                        BookConverter.toData(book)
                    }
                }
    }

    override fun loadBook(bookId: String): Flow<BookData?> {
        return bookDao.bookById(bookId)
                .map { book -> BookConverter.toData(book) }
    }

    override suspend fun remove(book: BookData) {
        bookDao.delete(BookConverter.fromData(book))
        fileHelper.deleteExistingCover(book)
    }
}
