package dominando.android.livros

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dominando.android.livros.binding.RecyclerViewBinding.BindableAdapter
import dominando.android.livros.databinding.ItemBookBinding
import dominando.android.presentation.binding.Book

class BookAdapter(private val onClick: (Book) -> Unit) :
    RecyclerView.Adapter<BookAdapter.ViewHolder>(),
    BindableAdapter<List<Book>> {

    private var books: List<Book>? = null

    override fun setData(data: List<Book>?) {
        books = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_book, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding?.apply {
            books?.get(position)?.let { currentBook ->
                book = currentBook
                executePendingBindings()
                root.setOnClickListener {
                    onClick(currentBook)
                }
            }
        }
    }

    fun getBook(position: Int) = books?.get(position)

    override fun getItemCount() = books?.size ?: 0

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ItemBookBinding>(view)
    }
}
