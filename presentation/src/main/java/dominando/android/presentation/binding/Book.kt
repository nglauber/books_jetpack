package dominando.android.presentation.binding

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import kotlinx.android.parcel.Parcelize

@Parcelize
class Book : BaseObservable(), Parcelable {
    @Bindable
    var id: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }
    @Bindable
    var title: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }
    @Bindable
    var author: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.author)
        }
    @Bindable
    var coverUrl: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.coverUrl)
        }
    @Bindable
    var pages: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.pages)
        }
    @Bindable
    var year: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.year)
        }
    @Bindable
    var publisher: Publisher? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.publisher)
        }
    @Bindable
    var available: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.available)
        }
    @Bindable
    var mediaType: MediaType = MediaType.PAPER
        set(value) {
            field = value
            notifyPropertyChanged(BR.mediaType)
        }
    @Bindable
    var rating: Float = 0f
        set(value) {
            field = value
            notifyPropertyChanged(BR.rating)
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Book
        if (id != other.id) return false
        if (title != other.title) return false
        if (author != other.author) return false
        if (coverUrl != other.coverUrl) return false
        if (pages != other.pages) return false
        if (year != other.year) return false
        if (publisher != other.publisher) return false
        if (available != other.available) return false
        if (mediaType != other.mediaType) return false
        if (rating != other.rating) return false
        return true
    }
    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + coverUrl.hashCode()
        result = 31 * result + pages
        result = 31 * result + year
        result = 31 * result + (publisher?.hashCode() ?: 0)
        result = 31 * result + available.hashCode()
        result = 31 * result + mediaType.hashCode()
        result = 31 * result + rating.hashCode()
        return result
    }
}
