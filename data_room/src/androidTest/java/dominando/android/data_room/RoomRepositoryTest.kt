package dominando.android.data_room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import dominando.android.data.model.Book
import dominando.android.data.model.MediaType
import dominando.android.data.model.Publisher
import dominando.android.data_room.database.AppDatabase
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
        val db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
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
    fun insertBook() {
        repo.saveBook(dummyBook).test().assertComplete()
    }

    @Test
    fun insertTheSameBookIdMustUpdateTheRecord() {
        val id = dummyBook.id
        repo.saveBook(dummyBook).test().assertComplete()
        val book1 = repo.loadBook(id).test().values().first()
        repo.saveBook(dummyBook).test().assertComplete()
        val book2 = repo.loadBook(id).test().values().first()
        assertEquals(book1, book2)
        assertEquals(book1, dummyBook)
        assertEquals(book2, dummyBook)
    }

    @Test
    fun insertedBookMustBeReturned() {
        val bookId = dummyBook.id
        repo.saveBook(dummyBook).test().assertComplete()
        val loadedBook = repo.loadBook(bookId).test().values().first()
        assertEquals(dummyBook, loadedBook)
    }

    @Test
    fun updateAllBookFields() {
        val bookId = dummyBook.id
        repo.saveBook(dummyBook).test().assertComplete()
        val updatedBook = newBook(bookId)
        repo.saveBook(updatedBook).test().assertComplete()
        val loadedBook = repo.loadBook(bookId).test().values().first()
        assertEquals(updatedBook, loadedBook)
    }

    @Test
    fun loadAllInsertedBooks() {
        val allBooks = listOf(
                newBook("1"),
                newBook("2"),
                newBook("3")
        )
        allBooks.forEach {
            repo.saveBook(it).test().assertComplete()
        }
        repo.loadBooks()
                .test()
                .assertValue {
                    allBooks.size == it.size && it.containsAll(allBooks)
                }
    }

    @Test
    fun removeBook() {
        repo.saveBook(dummyBook).test().assertComplete()
        repo.remove(dummyBook).test().assertComplete()
    }

    @Test
    fun removeBookWithCoverRemovesTheFile() {
        val context = InstrumentationRegistry.getTargetContext()
        val cover = File(context.filesDir, "${dummyBook.id}.jpg")
        Assert.assertTrue(cover.createNewFile())
        Assert.assertTrue(cover.exists())
        val book = dummyBook.copy(coverUrl = "file://${cover.absolutePath}")
        repo.saveBook(book).test().assertComplete()
        repo.remove(book).test().assertComplete()
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