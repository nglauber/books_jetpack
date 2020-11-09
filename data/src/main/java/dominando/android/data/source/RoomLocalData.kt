package dominando.android.data.source

import dominando.android.data.model.BookData
import kotlinx.coroutines.flow.Flow

interface RoomLocalData {
    fun loadBooks(): Flow<List<BookData>>
    fun loadBook(bookId: String): Flow<BookData?>
    suspend fun saveBook(book: BookData)
    suspend fun remove(book: BookData)
}