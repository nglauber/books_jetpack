package dominando.android.data.mapper

import dominando.android.data.factory.DataFactory
import dominando.android.data.model.BookData
import dominando.android.domain.entity.Book
import org.junit.Test
import kotlin.test.assertEquals

internal class BooksMapperTest {

    private val mapper: Mapper<BookData,Book> = BooksMapper()

    @Test
    fun testBookDomainEqualsData()  {
        // Given
        val expected = DataFactory.dummyBook()
        val book = DataFactory.dummyDataBook()

        // When
        val result = mapper.map(book)

        // Then
        assertEquals(expected, result)
    }
}