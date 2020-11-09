package dominando.android.data_room.converters

import androidx.room.TypeConverter
import dominando.android.data.model.MediaTypeData

internal class MediaTypeConverter {
    @TypeConverter
    fun fromMediaType(value: MediaTypeData): Int {
        return value.let { value.ordinal }
    }

    @TypeConverter
    fun toMediaType(value: Int): MediaTypeData {
        return MediaTypeData.values()[value]
    }
}
