package dominando.android.data

import dominando.android.data.model.Book
import kotlinx.coroutines.flow.Flow

interface BooksRepository {
    fun loadBooks(): Flow<List<Book>>
    fun loadBook(bookId: String): Flow<Book?>
    suspend fun saveBook(book: Book)
    suspend fun remove(book: Book)
}