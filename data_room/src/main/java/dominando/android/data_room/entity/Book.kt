package dominando.android.data_room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import dominando.android.data.model.MediaType
import dominando.android.data.model.Publisher
import dominando.android.data_room.converters.MediaTypeConverter

@Entity
@TypeConverters(MediaTypeConverter::class)
data class Book(
        @PrimaryKey
        var id: String,
        var title: String = "",
        var author: String = "",
        var coverUrl: String = "",
        var pages: Int = 0,
        var year: Int = 0,
        @Embedded(prefix = "publisher_")
        var publisher: Publisher,
        var available: Boolean = false,
        var mediaType: MediaType = MediaType.PAPER,
        var rating: Float = 0f
)