package dominando.android.data.mapper

import dominando.android.data.factory.DataFactory
import dominando.android.data.model.BookData
import dominando.android.domain.entity.Book
import org.junit.Test
import kotlin.test.assertEquals

class BooksDataMapperTest {

    private val mapper: Mapper<Book, BookData> = BooksDataMapper()

    @Test
    fun testBookDataEqualsDomain()  {
        // Given
        val expected = DataFactory.dummyDataBook()
        val book = DataFactory.dummyBook()

        // When
        val result = mapper.map(book)

        // Then
        assertEquals(expected, result)
    }
}