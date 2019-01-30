package dominando.android.presentation.livedata

open class LiveEvent<out T>(private val content: T) {

    var hasBeenConsumed = false
        private set // Allow external read but not write

    fun consumeEvent(): T? {
        val temp = peekContent()
        hasBeenConsumed = true
        return temp
    }

    fun peekContent(): T? = if (hasBeenConsumed) {
        null
    } else {
        content
    }
}