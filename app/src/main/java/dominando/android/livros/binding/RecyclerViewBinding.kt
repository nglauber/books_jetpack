package dominando.android.livros.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

object RecyclerViewBinding {

    interface BindableAdapter<T> {
        fun setData(data: T?)
    }

    @BindingAdapter("data")
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T> setRecyclerViewProperties(recyclerView: RecyclerView, data: T?) {
        if (recyclerView.adapter is BindableAdapter<*>) {
            (recyclerView.adapter as BindableAdapter<T?>).setData(data)
        }
    }
}
