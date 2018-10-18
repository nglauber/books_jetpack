package dominando.android.data_room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import dominando.android.data.model.MediaType
import dominando.android.data.model.Publisher
import dominando.android.data_room.database.AppDatabase
import dominando.android.data_room.entity.Book
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


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
            publisher = Publisher(UUID.randomUUID().toString(), "Novatec"),
            year = 2018,
            mediaType = MediaType.EBOOK,
            rating = 5f
    )

    @Before
    fun initDb() {
        appDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                AppDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    fun insertBook() {
        val test = appDatabase.bookDao().save(dummyBook).test()
        test.assertComplete()
    }

    @Test
    fun insertedBookMustBeReturned() {
        val bookId = dummyBook.id
        val testSave = appDatabase.bookDao().save(dummyBook).test()
        testSave.assertComplete()
        val loadedBook = appDatabase.bookDao().bookById(bookId).test()
        loadedBook.assertValue { it == dummyBook }
    }

    @Test
    fun updateAllBookFields() {
        val bookId = dummyBook.id
        val dao = appDatabase.bookDao()
        val testSave = dao.save(dummyBook).test()
        testSave.assertComplete()
        val updatedBook = dummyBook.copy(
                title = "Novo",
                author = "Nilson",
                coverUrl = "www.nglauber.com.br",
                pages = 1000,
                year = 2016,
                publisher = Publisher(UUID.randomUUID().toString(), "Test"),
                available = false,
                mediaType = MediaType.PAPER,
                rating = 2.5f
        )
        val testUpdate = dao.save(updatedBook).test()
        testUpdate.assertComplete()

        val testLoadBook = appDatabase.bookDao().bookById(bookId).test()
        testLoadBook.assertValue { it == updatedBook }
    }

    @Test
    fun loadAllInsertedBooks() {
        val dao = appDatabase.bookDao()

        val allBooks = listOf(
                dummyBook.copy(id = "1"),
                dummyBook.copy(id = "2"),
                dummyBook.copy(id = "3")
        )
        allBooks.forEach {
            val testSave = dao.save(it).test()
            testSave.assertComplete()
        }
        dao.bookByTitle()
                .test()
                .assertValue {
                    it.size == allBooks.size && it.containsAll(allBooks)
                }
    }

    @Test
    fun removeBook() {
        val dao = appDatabase.bookDao()
        val testSave = dao.save(dummyBook).test()
        testSave.assertComplete()
        val testDelete = dao.delete(dummyBook).test()
        testDelete.assertComplete()
    }
}