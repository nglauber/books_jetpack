package dominando.android.presentation.livedata

open class LiveEvent<out T>(private val content: T) {

    var hasBeenConsumed = false
        private set // Allow external read but not write

    fun consumeEvent(): T? {
        return if (hasBeenConsumed) {
            null
        } else {
            hasBeenConsumed = true
            content
        }
    }

    fun peekContent(): T = content
}