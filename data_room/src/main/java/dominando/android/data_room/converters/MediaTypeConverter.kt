package dominando.android.data_room.converters

import androidx.room.TypeConverter
import dominando.android.data.model.MediaType

class MediaTypeConverter {
    @TypeConverter
    fun fromMediaType(value: MediaType): Int {
        return value.let { value.ordinal }
    }

    @TypeConverter
    fun toMediaType(value: Int): MediaType {
        return MediaType.values()[value]
    }
}