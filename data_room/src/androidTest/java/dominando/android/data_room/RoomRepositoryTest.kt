package dominando.android.data_room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dominando.android.data.model.Book
import dominando.android.data.model.MediaType
import dominando.android.data.model.Publisher
import dominando.android.data_room.database.AppDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.*


@RunWith(AndroidJUnit4::class)
class RoomRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repo: RoomRepository
    private lateinit var dummyBook: Book

    @Before
    fun initDb() {
        val db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context,
                AppDatabase::class.java).build()
        repo = RoomRepository(db, LocalFileHelper())
        dummyBook = Book().apply {
            id = UUID.randomUUID().toString()
            title = "Dominando o Android"
            author = "Nelson Glauber"
            available = true
            coverUrl = ""
            pages = 954
            publisher = dominando.android.data.model.Publisher(UUID.randomUUID().toString(), "Novatec")
            year = 2018
            mediaType = dominando.android.data.model.MediaType.EBOOK
            rating = 5f
        }
    }

    @Test
    fun insertBook() = runBlocking {
        repo.saveBook(dummyBook)
    }

    @Test
    fun insertTheSameBookIdMustUpdateTheRecord() = runBlocking {
        val id = dummyBook.id
        repo.saveBook(dummyBook)
        val book1 = repo.loadBook(id).first()
        repo.saveBook(dummyBook)
        val book2 = repo.loadBook(id).first()
        assertEquals(book1, book2)
        assertEquals(book1, dummyBook)
        assertEquals(book2, dummyBook)
    }

    @Test
    fun insertedBookMustBeReturned() = runBlocking {
        val bookId = dummyBook.id
        repo.saveBook(dummyBook)
        val loadedBook = repo.loadBook(bookId).first()
        assertEquals(dummyBook, loadedBook)
    }

    @Test
    fun updateAllBookFields() = runBlocking {
        val bookId = dummyBook.id
        repo.saveBook(dummyBook)
        val updatedBook = newBook(bookId)
        repo.saveBook(updatedBook)
        val loadedBook = repo.loadBook(bookId).first()
        assertEquals(updatedBook, loadedBook)
    }

    @Test
    fun loadAllInsertedBooks() = runBlocking {
        val allBooks = listOf(
                newBook("1"),
                newBook("2"),
                newBook("3")
        )
        allBooks.forEach {
            repo.saveBook(it)
        }
        val list = repo.loadBooks().first()
        assertEquals(allBooks.size, list.size)
        assert(list.containsAll(allBooks))
    }

    @Test
    fun removeBook() = runBlocking {
        repo.saveBook(dummyBook)
        repo.remove(dummyBook)
    }

    @Test
    fun removeBookWithCoverRemovesTheFile() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val cover = File(context.filesDir, "${dummyBook.id}.jpg")
        Assert.assertTrue(cover.createNewFile())
        Assert.assertTrue(cover.exists())
        val book = dummyBook.copy(coverUrl = "file://${cover.absolutePath}")
        repo.saveBook(book)
        repo.remove(book)
        Assert.assertFalse(cover.exists())
    }

    private fun newBook(bookId: String) = Book().apply {
        id = bookId
        title = "Novo"
        author = "Nilson"
        coverUrl = ""
        pages = 1000
        year = 2016
        publisher = Publisher(UUID.randomUUID().toString(), "Test")
        available = false
        mediaType = MediaType.PAPER
        rating = 2.5f
    }
}