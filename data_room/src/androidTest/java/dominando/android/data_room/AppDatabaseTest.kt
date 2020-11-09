package dominando.android.data_room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dominando.android.data.model.MediaTypeData
import dominando.android.data.model.PublisherData
import dominando.android.data_room.database.AppDatabase
import dominando.android.data_room.entity.Book
import java.util.UUID
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var appDatabase: AppDatabase
    private val dummyBook = Book(
            id = UUID.randomUUID().toString(),
            title = "Dominando o Android",
            author = "Nelson Glauber",
            available = true,
            coverUrl = "",
            pages = 954,
            publisherData = PublisherData(UUID.randomUUID().toString(), "Novatec"),
            year = 2018,
            mediaTypeData = MediaTypeData.EBOOK,
            rating = 5f
    )

    @Before
    fun initDb() {
        appDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().context,
                AppDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    fun insertBook() = runBlocking {
        appDatabase.bookDao().save(dummyBook)
    }

    @Test
    fun insertedBookMustBeReturned() = runBlocking {
        val bookId = dummyBook.id
        appDatabase.bookDao().save(dummyBook)

        val book = appDatabase.bookDao().bookById(bookId).first()
        assertEquals(book, dummyBook)
    }

    @Test
    fun updateAllBookFields() = runBlocking {
        val bookId = dummyBook.id
        val dao = appDatabase.bookDao()
        dao.save(dummyBook)

        val updatedBook = dummyBook.copy(
                title = "Novo",
                author = "Nilson",
                coverUrl = "www.nglauber.com.br",
                pages = 1000,
                year = 2016,
                publisherData = PublisherData(UUID.randomUUID().toString(), "Test"),
                available = false,
                mediaTypeData = MediaTypeData.PAPER,
                rating = 2.5f
        )
        dao.save(updatedBook)

        val book = appDatabase.bookDao().bookById(bookId).first()
        assertEquals(book, updatedBook)
    }

    @Test
    fun loadAllInsertedBooks() = runBlocking {
        val dao = appDatabase.bookDao()

        val allBooks = listOf(
                dummyBook.copy(id = "1"),
                dummyBook.copy(id = "2"),
                dummyBook.copy(id = "3")
        )
        allBooks.forEach {
            dao.save(it)
        }
        val list = dao.bookByTitle().first()
        assertEquals(list.size, allBooks.size)
        assert(list.containsAll(allBooks))
    }

    @Test
    fun removeBook() = runBlocking {
        val dao = appDatabase.bookDao()
        dao.save(dummyBook)

        dao.delete(dummyBook)
    }
}
