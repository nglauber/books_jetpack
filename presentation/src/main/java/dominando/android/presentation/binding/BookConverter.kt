package dominando.android.presentation.binding

import dominando.android.data.model.Book
import dominando.android.data.model.MediaType
import dominando.android.data.model.Publisher
import dominando.android.presentation.binding.Book as BookBinding
import dominando.android.presentation.binding.MediaType as MediaTypeBinding
import dominando.android.presentation.binding.Publisher as PublisherBinding

object BookConverter {
    fun fromData(book: Book): BookBinding {
        return BookBinding().apply {
            id = book.id
            title = book.title
            author = book.author
            coverUrl = book.coverUrl
            pages = book.pages
            year = book.year
            publisher = PublisherBinding(book.publisher?.id ?: "", book.publisher?.name ?: "")
            available = book.available
            mediaType = MediaTypeBinding.values()[book.mediaType.ordinal]
            rating = book.rating
        }
    }

    fun toData(book: BookBinding): Book {
        return Book(
                id = book.id,
                title = book.title,
                author = book.author,
                coverUrl = book.coverUrl,
                pages = book.pages,
                year = book.year,
                publisher = Publisher(book.publisher?.id ?: "", book.publisher?.name ?: ""),
                available = book.available,
                mediaType = MediaType.values()[book.mediaType.ordinal],
                rating = book.rating
        )
    }
}
