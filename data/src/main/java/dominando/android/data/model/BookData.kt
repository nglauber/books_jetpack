package dominando.android.data.model

data class BookData(
    var id: String = "",
    var title: String = "",
    var author: String = "",
    var coverUrl: String = "",
    var pages: Int = 0,
    var year: Int = 0,
    var publisher: PublisherData? = null,
    var available: Boolean = false,
    var mediaType: MediaTypeData = MediaTypeData.PAPER,
    var rating: Float = 0f
)