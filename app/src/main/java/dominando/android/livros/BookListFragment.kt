package dominando.android.livros

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dominando.android.livros.common.BaseFragment
import dominando.android.livros.databinding.FragmentBookListBinding
import dominando.android.presentation.BookListViewModel
import dominando.android.presentation.ViewState
import org.koin.android.viewmodel.ext.android.viewModel

class BookListFragment : BaseFragment() {

    private val viewModel: BookListViewModel by viewModel()
    private lateinit var dataBinding: FragmentBookListBinding

    private val bookAdapter by lazy {
        BookAdapter { book ->
            router.showBookDetails(book)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_book_list,
                container,
                false) as FragmentBookListBinding

        dataBinding.run {
            lifecycleOwner = this@BookListFragment
            viewModel = this@BookListFragment.viewModel

            /* Init UI */
            initRecyclerView()
            initFab()
        }
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        viewModel.state().observe(viewLifecycleOwner, Observer { viewState ->
            viewState?.let {
                if (viewState.status == ViewState.Status.ERROR) {
                    Toast.makeText(requireContext(),
                            R.string.message_error_load_books,
                            Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.removeOperation().observe(viewLifecycleOwner, Observer { event ->
            when (event.status) {
                ViewState.Status.SUCCESS -> {
                    Snackbar.make(dataBinding.rvBooks, R.string.message_book_removed,
                            Snackbar.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(requireContext(),
                            R.string.message_error_delete_book, Toast.LENGTH_SHORT).show()
                }
            }
        })
        lifecycle.addObserver(viewModel)
    }

    private fun initRecyclerView() {
        dataBinding.rvBooks.adapter = bookAdapter
        attachSwipeToRecyclerView()
    }

    private fun initFab() {
        dataBinding.fabAdd.setOnClickListener {
            router.showBookForm(null)
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
        itemTouchHelper.attachToRecyclerView(dataBinding.rvBooks)
    }

    private fun deleteBookFromPosition(position: Int) {
        bookAdapter.getBook(position)?.let { book ->
            viewModel.remove(book)
        }
    }
}
