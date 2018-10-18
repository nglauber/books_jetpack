package dominando.android.livros

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dominando.android.presentation.BookListViewModel
import dominando.android.presentation.BookVmFactory
import dominando.android.presentation.ViewState
import dominando.android.presentation.binding.Book
import kotlinx.android.synthetic.main.fragment_book_list.*

class BookListFragment : BaseFragment() {
    private val viewModel: BookListViewModel by lazy {
        ViewModelProviders.of(this,
                BookVmFactory(requireActivity().application)
        ).get(BookListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_book_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateList(emptyList())
        fabAdd.setOnClickListener {
            newBook()
        }
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_sign_out) {
            FirebaseAuth.getInstance().signOut()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        viewModel.getState().observe(this, Observer { viewState ->
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
        viewModel.removeOperation().observe(this, Observer { event ->
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
                viewDetails(book)
            }
            attachSwipeToRecyclerView()
        }
    }

    private fun attachSwipeToRecyclerView() {
        val swipe = object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder,
                                  direction: Int) {
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

    private fun newBook() {
        navController.navigate(R.id.action_list_to_form)
    }

    private fun viewDetails(book: Book) {
        val args = Bundle().apply {
            putParcelable("book", book)
        }
        navController.navigate(R.id.action_list_to_details, args)
    }
}