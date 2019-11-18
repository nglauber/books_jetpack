package dominando.android.data_room

import dominando.android.data.model.Book
import dominando.android.data_room.entity.Book as BookEntity
import java.util.UUID

object BookConverter {
    fun fromData(binding: Book) = BookEntity(
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

    fun toData(entity: BookEntity) = Book().apply {
        id = entity.id
        title = entity.title
        author = entity.author
        coverUrl = entity.coverUrl
        pages = entity.pages
        year = entity.year
        publisher = entity.publisher
        available = entity.available
        mediaType = entity.mediaType
        rating = entity.rating
    }
}
