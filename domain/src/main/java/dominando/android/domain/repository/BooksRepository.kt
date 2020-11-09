package dominando.android.domain.repository

import dominando.android.domain.entity.Book
import kotlinx.coroutines.flow.Flow

interface BooksRepository {
    fun loadBooks(): Flow<List<Book>>
    fun loadBook(bookId: String): Flow<Book?>
    suspend fun saveBook(book: Book)
    suspend fun remove(book: Book)
}
