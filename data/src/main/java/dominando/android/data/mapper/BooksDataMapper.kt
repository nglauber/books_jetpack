package dominando.android.data.mapper

import dominando.android.data.model.BookData
import dominando.android.data.model.MediaTypeData
import dominando.android.data.model.PublisherData
import dominando.android.domain.entity.Book
import dominando.android.domain.entity.MediaType
import dominando.android.domain.entity.Publisher

class BooksDataMapper : Mapper<Book, BookData> {

    override fun map(source: Book): BookData {
        return BookData(
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

    private fun mapPublisher(publisher: Publisher?): PublisherData? {
        return publisher?.run {
            PublisherData(
                id = id,
                name = name
            )
        }
    }

    private fun mapMediaType(mediaType: MediaType): MediaTypeData{
        return when (mediaType) {
            MediaType.PAPER -> MediaTypeData.PAPER
            MediaType.EBOOK -> MediaTypeData.EBOOK
        }
    }
}