package dominando.android.data.mapper

internal interface Mapper<I, O> {
    fun map(source: I): O
}