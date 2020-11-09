package dominando.android.data_room

import dominando.android.data.model.BookData
import dominando.android.data_room.entity.Book as BookEntity
import java.util.UUID

internal object BookConverter {

    fun fromData(binding: BookData) = BookEntity(
            if (binding.id.isBlank()) UUID.randomUUID().toString() else binding.id,
            binding.title,
            binding.author,
            binding.coverUrl,
            binding.pages,
            binding.year,
            binding.publisher!!,
            binding.available,
            binding.mediaType,
            binding.rating
    )

    fun toData(entity: BookEntity) = BookData().apply {
        id = entity.id
        title = entity.title
        author = entity.author
        coverUrl = entity.coverUrl
        pages = entity.pages
        year = entity.year
        publisher = entity.publisherData
        available = entity.available
        mediaType = entity.mediaTypeData
        rating = entity.rating
    }
}
