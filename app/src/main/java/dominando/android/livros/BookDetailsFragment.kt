package dominando.android.livros

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dominando.android.livros.databinding.FragmentBookDetailsBinding
import dominando.android.presentation.BookDetailsViewModel
import dominando.android.presentation.BookVmFactory
import dominando.android.presentation.ViewState
import dominando.android.presentation.binding.Book

class BookDetailsFragment : BaseFragment() {
    private val viewModel: BookDetailsViewModel by viewModels {
        BookVmFactory(requireActivity().application, this.router)
    }

    private lateinit var binding: FragmentBookDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_book_details, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val book = arguments?.getParcelable<Book>("book")
        if (book != null) {
            binding.book = book
        }
        init()
    }

    private fun init() {
        viewModel.getState().observe(viewLifecycleOwner, Observer { viewState ->
            when (viewState.status) {
                ViewState.Status.SUCCESS -> binding.book = viewState.data
                ViewState.Status.LOADING -> {} /* TODO */
                ViewState.Status.ERROR -> {}/* TODO */
            }
        })
        val book = arguments?.getParcelable<Book>("book")
        book?.let {
            viewModel.loadBook(book.id)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_edit_book) {
            binding.book?.let {
                viewModel.editBook(it)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
