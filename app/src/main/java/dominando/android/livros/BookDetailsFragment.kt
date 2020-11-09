package dominando.android.livros

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import dominando.android.livros.common.BaseFragment
import dominando.android.livros.common.Constants.EXTRA_BOOK
import dominando.android.livros.databinding.FragmentBookDetailsBinding
import dominando.android.presentation.BookDetailsViewModel
import dominando.android.presentation.binding.Book
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class BookDetailsFragment : BaseFragment() {

    private val viewModel: BookDetailsViewModel by viewModel {
        parametersOf(arguments?.getParcelable<Book>(EXTRA_BOOK)?.id)
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
        val binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_book_details,
                container, false) as FragmentBookDetailsBinding

        return binding.run {
            lifecycleOwner = this@BookDetailsFragment
            viewModel = this@BookDetailsFragment.viewModel
            root
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_edit_book) {
            viewModel.book.value?.let {
                router.showBookForm(it)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
