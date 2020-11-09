package dominando.android.data_room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import dominando.android.data.model.MediaTypeData
import dominando.android.data.model.PublisherData
import dominando.android.data_room.converters.MediaTypeConverter

@Entity
@TypeConverters(MediaTypeConverter::class)
internal data class Book(
        @PrimaryKey
    var id: String,
        var title: String = "",
        var author: String = "",
        var coverUrl: String = "",
        var pages: Int = 0,
        var year: Int = 0,
        @Embedded(prefix = "publisher_")
    var publisherData: PublisherData,
        var available: Boolean = false,
        var mediaTypeData: MediaTypeData = MediaTypeData.PAPER,
        var rating: Float = 0f
)
