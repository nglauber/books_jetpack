package dominando.android.data.factory

import dominando.android.data.model.BookData
import dominando.android.data.model.MediaTypeData
import dominando.android.data.model.PublisherData
import dominando.android.domain.entity.Book
import dominando.android.domain.entity.MediaType
import dominando.android.domain.entity.Publisher
import java.util.*

object DataFactory {

    fun dummyDataBookList() = listOf(
            BookData().apply {
                id = "48546423158"
                title = "Dominando o Android"
                author = "Nelson Glauber"
                available = true
                coverUrl = ""
                pages = 954
                publisher = PublisherData("15638546", "Novatec")
                year = 2018
                mediaType = MediaTypeData.PAPER
                rating = 5f
            },
            BookData().apply {
                id = "48546423159"
                title = "Clean Code"
                author = "Uncle Bob"
                available = true
                coverUrl = ""
                pages = 465
                publisher = PublisherData("15638547", "Outro")
                year = 2009
                mediaType = MediaTypeData.EBOOK
                rating = 5f
            }
    )

    fun dummyDataBook() = BookData().apply {
        id = "48546423158"
        title = "Dominando o Android"
        author = "Nelson Glauber"
        available = true
        coverUrl = ""
        pages = 954
        publisher = PublisherData("14556513", "Novatec")
        year = 2018
        mediaType = MediaTypeData.EBOOK
        rating = 5f
    }

    fun dummyBookList() = listOf(
            Book().apply {
                id = "48546423158"
                title = "Dominando o Android"
                author = "Nelson Glauber"
                available = true
                coverUrl = ""
                pages = 954
                publisher = Publisher("15638546", "Novatec")
                year = 2018
                mediaType = MediaType.PAPER
                rating = 5f
            },
            Book().apply {
                id = "48546423159"
                title = "Clean Code"
                author = "Uncle Bob"
                available = true
                coverUrl = ""
                pages = 465
                publisher = Publisher("15638547", "Outro")
                year = 2009
                mediaType = MediaType.EBOOK
                rating = 5f
            }
    )

    fun dummyBook() = Book().apply {
        id = "48546423158"
        title = "Dominando o Android"
        author = "Nelson Glauber"
        available = true
        coverUrl = ""
        pages = 954
        publisher = Publisher("14556513", "Novatec")
        year = 2018
        mediaType = MediaType.EBOOK
        rating = 5f
    }
}