package dominando.android.livros

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dominando.android.livros.common.BaseFragment
import dominando.android.presentation.BookListViewModel
import dominando.android.presentation.ViewState
import dominando.android.presentation.binding.Book
import kotlinx.android.synthetic.main.fragment_book_list.*
import org.koin.android.ext.android.inject

class BookListFragment : BaseFragment() {
    private val viewModel: BookListViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateList(emptyList())
        fabAdd.setOnClickListener {
            router.showBookForm(null)
        }
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_sign_out) {
            auth.signOut()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        viewModel.getState().observe(viewLifecycleOwner, Observer { viewState ->
            viewState?.let {
                when (viewState.status) {
                    ViewState.Status.SUCCESS -> updateList(viewState.data)
                    ViewState.Status.LOADING -> { /* TODO */
                    }
                    ViewState.Status.ERROR ->
                        Toast.makeText(requireContext(),
                                R.string.message_error_load_books,
                                Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.removeOperation().observe(viewLifecycleOwner, Observer { event ->
            event.consumeEvent()?.let { viewState ->
                when (viewState.status) {
                    ViewState.Status.SUCCESS -> {
                        Snackbar.make(rvBooks, R.string.message_book_removed, Snackbar.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(requireContext(),
                                R.string.message_error_delete_book, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        lifecycle.addObserver(viewModel)
    }

    private fun updateList(books: List<Book>?) {
        books?.let {
            val columns =
                    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 1
                    else 2
            rvBooks.layoutManager = GridLayoutManager(requireContext(), columns)
            rvBooks.adapter = BookAdapter(books) { book ->
                router.showBookDetails(book)
            }
            attachSwipeToRecyclerView()
        }
    }

    private fun attachSwipeToRecyclerView() {
        val swipe = object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
                val position = viewHolder.adapterPosition
                deleteBookFromPosition(position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipe)
        itemTouchHelper.attachToRecyclerView(rvBooks)
    }

    private fun deleteBookFromPosition(position: Int) {
        val adapter = rvBooks.adapter as BookAdapter
        val book = adapter.books[position]
        viewModel.remove(book)
    }
}
