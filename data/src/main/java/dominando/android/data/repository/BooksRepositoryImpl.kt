package dominando.android.data.repository

import dominando.android.data.mapper.Mapper
import dominando.android.data.model.BookData
import dominando.android.data.source.RoomLocalData
import dominando.android.domain.entity.Book
import dominando.android.domain.repository.BooksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class BooksRepositoryImpl(
   private val localData: RoomLocalData,
   private val entityMapper: Mapper<BookData, Book>,
   private val dataMapper: Mapper<Book, BookData>
) : BooksRepository {

    override fun loadBooks(): Flow<List<Book>> {
        return localData.loadBooks().map { it.map(entityMapper::map) }
    }

    override fun loadBook(bookId: String): Flow<Book?> {
        return localData.loadBook(bookId).map{ it?.let(entityMapper::map) }
    }

    override suspend fun saveBook(book: Book) {
        return localData.saveBook(dataMapper.map(book))
    }

    override suspend fun remove(book: Book) {
        return localData.remove(dataMapper.map(book))
    }
}