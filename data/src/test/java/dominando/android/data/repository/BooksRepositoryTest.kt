package dominando.android.data.repository

import dominando.android.data.factory.DataFactory
import dominando.android.data.mapper.BooksDataMapper
import dominando.android.data.mapper.BooksMapper
import dominando.android.data.source.RoomLocalData
import dominando.android.domain.repository.BooksRepository
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertEquals as kotlinAssertEquals

internal class BooksRepositoryTest {

    private val entityMapper = BooksMapper()
    private val dataMapper = BooksDataMapper()
    private val roomLocalData = mockk<RoomLocalData>(relaxed = true)
    private val booksRepository: BooksRepository =
            BooksRepositoryImpl(roomLocalData, entityMapper, dataMapper)

    @After
    fun tearDown() {
        clearMocks(roomLocalData)
    }

    @Test
    fun testLoadedAllBooks() = runBlocking {
        // Given
        val expectedBooks = DataFactory.dummyBookList()

        coEvery { roomLocalData.loadBooks() } returns flowOf(DataFactory.dummyDataBookList())

        // When
        val result = booksRepository.loadBooks().first()

        // Then
        assertEquals(expectedBooks.size, result.size)
        kotlinAssertEquals(expectedBooks, result)
    }

    @Test
    fun testLoadedSingleBook() = runBlocking {
        // Given
        val id = "0"
        val expectedBook = DataFactory.dummyBook()

        coEvery { roomLocalData.loadBook(id) } returns flowOf(DataFactory.dummyDataBook())

        // When
        val result = booksRepository.loadBook(id).first()

        // Then
        assertEquals(expectedBook, result)
    }

    @Test
    fun testSavedBook() = runBlocking {
        // Given
        val expectedBook = DataFactory.dummyDataBook()
        val book = DataFactory.dummyBook()

        // When
        booksRepository.saveBook(book)

        // Then
        coVerify { roomLocalData.saveBook(expectedBook) }
    }

    @Test
    fun testRemoveBook() = runBlocking {
        // Given
        val expectedBook = DataFactory.dummyDataBook()
        val book = DataFactory.dummyBook()

        // When
        booksRepository.remove(book)

        // Then
        coVerify { roomLocalData.remove(expectedBook) }
    }

}