package dominando.android.data.mapper

import dominando.android.data.model.BookData
import dominando.android.data.model.MediaTypeData
import dominando.android.data.model.PublisherData
import dominando.android.domain.entity.Book
import dominando.android.domain.entity.MediaType
import dominando.android.domain.entity.Publisher

internal class BooksMapper : Mapper<BookData, Book> {

    override fun map(source: BookData): Book {
        return Book(
            id = source.id,
            title = source.title,
            author = source.author,
            coverUrl = source.coverUrl,
            pages = source.pages,
            year = source.year,
            publisher = mapPublisher(source.publisher),
            available = source.available,
            mediaType = mapMediaType(source.mediaType),
            rating = source.rating
        )
    }

    private fun mapPublisher(publisher: PublisherData?): Publisher? {
        return publisher?.run {
            Publisher(
                id = id,
                name = name
            )
        }
    }

    private fun mapMediaType(mediaType: MediaTypeData): MediaType {
        return when (mediaType) {
            MediaTypeData.PAPER -> MediaType.PAPER
            MediaTypeData.EBOOK -> MediaType.EBOOK
        }
    }
}